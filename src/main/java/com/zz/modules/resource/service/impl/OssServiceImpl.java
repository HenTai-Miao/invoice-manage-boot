package com.zz.modules.resource.service.impl;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.zz.common.constant.InvoiceInfoConstant;
import com.zz.common.constant.OssMinioConstant;
import com.zz.common.error.ServiceException;
import com.zz.common.menus.SubjectType;
import com.zz.common.util.ZipUtils;
import com.zz.modules.invoicemanage.converter.InvoiceInfoConverter;
import com.zz.modules.invoicemanage.entity.InvoiceInfoEntity;
import com.zz.modules.invoicemanage.request.dto.EInvoiceParser;
import com.zz.modules.invoicemanage.request.dto.FileGroupInfo;
import com.zz.modules.invoicemanage.service.IInvoiceInfoService;
import com.zz.modules.resource.entity.AttachEntity;
import com.zz.modules.resource.mapper.AttachMapper;
import com.zz.modules.resource.service.IOssService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/12
 * Time：02:10
 * ================================
 */
@Slf4j
@Service
@AllArgsConstructor
public class OssServiceImpl implements IOssService {

    private final MinioClient minioClient;

    private final OssMinioConstant ossMinioConstant;

    private final IInvoiceInfoService IInvoiceInfoService;

    private final AttachMapper attachMapper;

    @Override
    public String invoicePutFile(MultipartFile file) {
        // 检查文件是否为空
        if (ObjectUtil.isEmpty(file)) {
            throw new ServiceException("文件为空");
        }
        Path tempDir = null;
        try {
            // 检查文件名是否存在且为ZIP文件
            if (!StringUtils.hasText(file.getOriginalFilename()) ||
                    !file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
                throw new ServiceException("文件后缀不被允许");
            }

            // 创建临时目录用于解压
            tempDir = Files.createTempDirectory("invoice_extract_");

            // 解压ZIP文件到临时目录，并按基础文件名分组
            List<FileGroupInfo> fileGroups = ZipUtils.extractFileGroupsToDirectory(
                    file.getInputStream(),
                    tempDir
            );
            AttachEntity attachEntity;
            Long xmlFileId;
            Long pdfFileId = null;
            Long ofdFileId = null;
            // 处理每个文件组
            for (FileGroupInfo fileGroup : fileGroups) {
                boolean isExists = IInvoiceInfoService.isExists(fileGroup.getBaseName());
                if (isExists) {
                    log.info("发票【{}】已存在，跳过处理", fileGroup.getBaseName());
                    continue;
                }
                // 必须有XML文件才处理该组
                if (fileGroup.hasXml()) {
                    // 为每组创建独立的文件key映射
                    // 上传XML文件
                    attachEntity = putFile(fileGroup.getXmlFilePath());
                    xmlFileId = attachEntity.getId();
                    // 上传PDF文件（如果存在）
                    if (fileGroup.hasPdf()) {
                        attachEntity = putFile(fileGroup.getPdfFilePath());
                        pdfFileId = attachEntity.getId();
                    }
                    // 上传OFD文件（如果存在）
                    if (fileGroup.hasOfd()) {
                        attachEntity = putFile(fileGroup.getOfdFilePath());
                        ofdFileId = attachEntity.getId();
                    }
                    // 解析XML内容并保存发票信息
                    String xmlContent = new String(Files.readAllBytes(Paths.get(fileGroup.getXmlFilePath())), StandardCharsets.UTF_8);
                    // 解析整体标签
                    Map<String, String> tags = EInvoiceParser.extractMultipleTags(xmlContent);
                    // 解析所有项目标签
                    List<Map<String, String>> items = EInvoiceParser.extractAllItems(xmlContent);
                    // 统一解析发票相关枚举类型
                    Map<String, String> enumMap = EInvoiceParser.parseInvoiceEnums(xmlContent);
                    // 获取发票类型名称
                    String eInvoiceTypeName = enumMap.get(InvoiceInfoConstant.E_INVOICE_TYPE);
                    // 获取增值税类型名称
                    String vatType = enumMap.get(InvoiceInfoConstant.GENERAL_OR_SPECIAL_V_A_T);
                    // 获取纳税人类型名称
                    String taxpayerType = enumMap.get(InvoiceInfoConstant.TAXPAYER_TYPE);
                    InvoiceInfoEntity invoiceInfoEntity = InvoiceInfoConverter.convertToEntity(tags);
                    // 判断是个人还是企业
                    boolean validCard = IdcardUtil.isValidCard(invoiceInfoEntity.getBuyerIdNum());
                    invoiceInfoEntity.setBuyerSubjectType(validCard ? SubjectType.PERSON.getValue() : SubjectType.ENTERPRISE.getValue());
                    // 设置发票类型
                    invoiceInfoEntity.setEInvoiceType(eInvoiceTypeName);
                    // 设置增值税类型
                    invoiceInfoEntity.setGeneralOrSpecialVat(vatType);
                    // 设置纳税人类型
                    invoiceInfoEntity.setTaxpayerType(taxpayerType);
                    // 设置项目信息
                    invoiceInfoEntity.setIssuItemInformation(JSON.toJSONString(items));
                    // 设置XML文件ID
                    invoiceInfoEntity.setXmlFileId(xmlFileId);
                    // 设置PDF文件ID
                    invoiceInfoEntity.setPdfFileId(pdfFileId);
                    // 设置OFD文件ID
                    invoiceInfoEntity.setOfdFileId(ofdFileId);
                    IInvoiceInfoService.save(invoiceInfoEntity);
                }
            }
        } catch (Exception e) {
            log.error("处理ZIP文件时出错: ", e);
            throw new ServiceException("处理ZIP文件时出错");
        } finally {
            // 删除临时目录及其内容
            if (tempDir != null) {
                try {
                    Thread.sleep(100);
                    deleteDirectoryRecursively(tempDir);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.warn("删除临时目录时线程被中断");
                } catch (IOException e) {
                    log.warn("删除临时目录时出错: ", e);
                }
            }
        }
        return "";
    }

    @Override
    public void downloadFile(String filePath, HttpServletResponse response) {

        String fileKey = filePath.replace(ossMinioConstant.getDownloadPath(), "");
        try {
            // 从MinIO获取文件流
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(ossMinioConstant.getBucketName())
                            .object(fileKey)
                            .build()
            );

            // 设置响应头
            String fileName = fileKey.substring(fileKey.lastIndexOf("/") + 1);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // 将文件流写入响应
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            response.getOutputStream().flush();
            inputStream.close();
        } catch (Exception e) {
            log.error("下载文件时出错: ", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "下载文件时出错: " + e.getMessage());
            } catch (IOException ioException) {
                log.error("发送错误响应时出错: ", ioException);
            }
        }
    }

    /**
     * 统一文件上传方法
     *
     * @param filePath 文件路径
     * @return 文件key
     */
    @SneakyThrows
    private AttachEntity putFile(String filePath) {
        // 获取文件扩展名
        String fileExtension = getFileExtension(filePath);
        // 生成日期前缀
        String datePrefix = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // 生成对象名称（带日期前缀）
        String objectName = ossMinioConstant.getFilePath() + datePrefix + "/" + UUID.randomUUID().toString() + "." + fileExtension;

        // 上传文件 - ensure FileInputStream is properly closed
        try (FileInputStream fis = new FileInputStream(filePath)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(ossMinioConstant.getBucketName())
                            .object(objectName)
                            .stream(fis, Files.size(Paths.get(filePath)), -1)
                            .build()
            );
        }
        int lastBackslashIndex = filePath.lastIndexOf("\\");
        String originalFileName = filePath.substring(lastBackslashIndex + 1);
        AttachEntity attachEntity = new AttachEntity();
        attachEntity.setLink(ossMinioConstant.getDownloadPath() + "/" + objectName);
        attachEntity.setName(objectName);
        attachEntity.setOriginalName(originalFileName);
        attachEntity.setExtension(fileExtension);
//        attachEntity.setAttachSize(String.valueOf(Files.size(Paths.get(filePath))));
        attachMapper.insert(attachEntity);
        // 返回文件key而不是下载URL
        return attachEntity;
    }

    /**
     * 获取文件扩展名
     *
     * @param filePath 文件路径
     * @return 文件扩展名
     */
    private String getFileExtension(String filePath) {
        if (filePath == null || filePath.lastIndexOf(".") == -1) {
            return "";
        }
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    /**
     * 递归删除目录及其内容
     *
     * @param directoryPath 目录路径
     * @throws IOException IO异常
     */
    private void deleteDirectoryRecursively(Path directoryPath) throws IOException {
        if (Files.exists(directoryPath)) {
            // First, try to delete using Files.deleteIfExists which might be more reliable
            Files.walk(directoryPath)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            // If Files.deleteIfExists fails, try the traditional approach
                            boolean deleted = path.toFile().delete();
                            if (!deleted) {
                                log.warn("无法删除文件或目录: " + path.toAbsolutePath());
                            }
                        }
                    });
        }
    }

    /**
     * 安全地将字符串转换为BigDecimal
     *
     * @param value 字符串值
     * @return BigDecimal值或null（如果转换失败）
     */
    private BigDecimal convertToBigDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            log.warn("无法解析数值字段: " + value);
            return null;
        }
    }

    @Override
    public AttachEntity detail(Long id) {
        return attachMapper.selectById(id);
    }
}