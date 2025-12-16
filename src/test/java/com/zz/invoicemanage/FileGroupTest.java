package com.zz.invoicemanage;

import com.zz.modules.invoicemanage.request.dto.FileGroupInfo;
import com.zz.common.util.ZipUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileGroupTest {

    @Test
    public void testExtractFileGroupsToDirectory() throws Exception {
        // 创建临时目录
        Path tempDir = Files.createTempDirectory("zip_group_test_");
        
        // 创建测试文件组1 (使用新的命名规则)
        Path xmlFile1 = tempDir.resolve("dzfp_25112000000270404395_invoice1.xml");
        Path pdfFile1 = tempDir.resolve("dzfp_25112000000270404395_invoice1.pdf");
        Path ofdFile1 = tempDir.resolve("dzfp_25112000000270404395_invoice1.ofd");
        
        // 创建测试文件组2 (使用相同的分组ID)
        Path xmlFile2 = tempDir.resolve("dzfp_25112000000270404395_invoice2.xml");
        Path pdfFile2 = tempDir.resolve("dzfp_25112000000270404395_invoice2.pdf");
        Path ofdFile2 = tempDir.resolve("dzfp_25112000000270404395_invoice2.ofd");
        
        // 创建测试文件组3 (使用不同的分组ID)
        Path xmlFile3 = tempDir.resolve("dzfp_99999999999999999999_invoice3.xml");
        Path pdfFile3 = tempDir.resolve("dzfp_99999999999999999999_invoice3.pdf");
        Path ofdFile3 = tempDir.resolve("dzfp_99999999999999999999_invoice3.ofd");
        
        // 写入测试内容
        Files.write(xmlFile1, "<root><test>XML Content 1</test></root>".getBytes());
        Files.write(pdfFile1, "PDF Content 1".getBytes());
        Files.write(ofdFile1, "OFD Content 1".getBytes());
        
        Files.write(xmlFile2, "<root><test>XML Content 2</test></root>".getBytes());
        Files.write(pdfFile2, "PDF Content 2".getBytes());
        Files.write(ofdFile2, "OFD Content 2".getBytes());
        
        Files.write(xmlFile3, "<root><test>XML Content 3</test></root>".getBytes());
        Files.write(pdfFile3, "PDF Content 3".getBytes());
        Files.write(ofdFile3, "OFD Content 3".getBytes());
        
        // 创建ZIP文件
        Path zipFile = tempDir.resolve("test_groups.zip");
        createZipFile(zipFile, xmlFile1, pdfFile1, ofdFile1, xmlFile2, pdfFile2, ofdFile2, xmlFile3, pdfFile3, ofdFile3);
        
        // 创建解压目标目录
        Path extractDir = Files.createTempDirectory("extract_group_test_");
        
        // 测试分组解压功能
        try (FileInputStream fis = new FileInputStream(zipFile.toFile())) {
            List<FileGroupInfo> fileGroups = ZipUtils.extractFileGroupsToDirectory(fis, extractDir);
            
            // 验证结果 - 应该有2个组（前两个文件属于同一组，第三个文件属于另一组）
            assertEquals(2, fileGroups.size());
            
            // 查找特定的组
            FileGroupInfo group1 = null;
            FileGroupInfo group2 = null;
            
            for (FileGroupInfo group : fileGroups) {
                if ("25112000000270404395".equals(group.getBaseName())) {
                    group1 = group;
                } else if ("99999999999999999999".equals(group.getBaseName())) {
                    group2 = group;
                }
            }
            
            // 验证第一组（应该包含来自两个文件的XML）
            assertNotNull(group1);
            assertEquals("25112000000270404395", group1.getBaseName());
            assertTrue(group1.hasXml());
            assertTrue(group1.hasPdf());
            assertTrue(group1.hasOfd());
            
            // 验证第二组
            assertNotNull(group2);
            assertEquals("99999999999999999999", group2.getBaseName());
            assertTrue(group2.hasXml());
            assertTrue(group2.hasPdf());
            assertTrue(group2.hasOfd());
            
            System.out.println("处理了 " + fileGroups.size() + " 个文件组:");
            for (FileGroupInfo group : fileGroups) {
                System.out.println("  组ID: " + group.getBaseName());
                System.out.println("    XML: " + group.getXmlFilePath());
                System.out.println("    PDF: " + group.getPdfFilePath());
                System.out.println("    OFD: " + group.getOfdFilePath());
            }
        }
        
        // 清理临时文件
        Files.deleteIfExists(xmlFile1);
        Files.deleteIfExists(pdfFile1);
        Files.deleteIfExists(ofdFile1);
        Files.deleteIfExists(xmlFile2);
        Files.deleteIfExists(pdfFile2);
        Files.deleteIfExists(ofdFile2);
        Files.deleteIfExists(xmlFile3);
        Files.deleteIfExists(pdfFile3);
        Files.deleteIfExists(ofdFile3);
        Files.deleteIfExists(zipFile);
        Files.deleteIfExists(tempDir);
    }
    
    @Test
    public void testExtractFileGroupsToDirectoryWithChineseNames() throws Exception {
        // 创建临时目录
        Path tempDir = Files.createTempDirectory("zip_chinese_test_");
        
        // 创建带有中文名称的测试文件组
        Path xmlFile = tempDir.resolve("发票1.xml");
        Path pdfFile = tempDir.resolve("发票1.pdf");
        Path ofdFile = tempDir.resolve("发票1.ofd");
        
        // 写入测试内容
        Files.write(xmlFile, "<root><test>XML Content 中文</test></root>".getBytes("UTF-8"));
        Files.write(pdfFile, "PDF Content 中文".getBytes("UTF-8"));
        Files.write(ofdFile, "OFD Content 中文".getBytes("UTF-8"));
        
        // 创建ZIP文件
        Path zipFile = tempDir.resolve("test_chinese.zip");
        createZipFile(zipFile, xmlFile, pdfFile, ofdFile);
        
        // 创建解压目标目录
        Path extractDir = Files.createTempDirectory("extract_chinese_test_");
        
        // 测试分组解压功能（中文文件名）
        try (FileInputStream fis = new FileInputStream(zipFile.toFile())) {
            List<FileGroupInfo> fileGroups = ZipUtils.extractFileGroupsToDirectory(fis, extractDir);
            
            // 验证结果
            assertEquals(1, fileGroups.size());
            
            // 验证文件组
            FileGroupInfo group = fileGroups.get(0);
            assertEquals("发票1", group.getBaseName()); // 对于没有下划线的文件名，使用整个基础文件名
            assertTrue(group.hasXml());
            assertTrue(group.hasPdf());
            assertTrue(group.hasOfd());
            
            // 验证文件内容
            String xmlContent = new String(Files.readAllBytes(Paths.get(group.getXmlFilePath())), "UTF-8");
            assertEquals("<root><test>XML Content 中文</test></root>", xmlContent);
            
            System.out.println("处理了 " + fileGroups.size() + " 个中文文件组:");
            System.out.println("  组名: " + group.getBaseName());
        }
        
        // 清理临时文件
        Files.deleteIfExists(xmlFile);
        Files.deleteIfExists(pdfFile);
        Files.deleteIfExists(ofdFile);
        Files.deleteIfExists(zipFile);
        Files.deleteIfExists(tempDir);
    }
    
    /**
     * 创建包含多个文件的ZIP文件
     */
    private static void createZipFile(Path zipFile, Path... files) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            byte[] buffer = new byte[1024];
            
            for (Path file : files) {
                // 添加文件到ZIP
                ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
                zos.putNextEntry(zipEntry);
                
                // 读取文件内容并写入ZIP
                try (FileInputStream fis = new FileInputStream(file.toFile())) {
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }
                
                zos.closeEntry();
            }
        }
    }
}