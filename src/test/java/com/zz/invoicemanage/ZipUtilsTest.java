package com.zz.invoicemanage;

import com.zz.modules.invoicemanage.request.dto.ExtractedFileInfo;
import com.zz.common.util.ZipUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ZipUtilsTest {

    @Test
    public void testExtractAllFilesToDirectory() throws Exception {
        // 创建临时目录
        Path tempDir = Files.createTempDirectory("zip_test_");
        
        // 创建测试文件
        Path xmlFile = tempDir.resolve("test.xml");
        Path pdfFile = tempDir.resolve("test.pdf");
        Path ofdFile = tempDir.resolve("test.ofd");
        
        // 写入测试内容
        Files.write(xmlFile, "<root><test>XML Content</test></root>".getBytes());
        Files.write(pdfFile, "PDF Content".getBytes());
        Files.write(ofdFile, "OFD Content".getBytes());
        
        // 创建ZIP文件
        Path zipFile = tempDir.resolve("test.zip");
        createZipFile(zipFile, xmlFile, pdfFile, ofdFile);
        
        // 创建解压目标目录
        Path extractDir = Files.createTempDirectory("extract_test_");
        
        // 测试解压功能
        try (FileInputStream fis = new FileInputStream(zipFile.toFile())) {
            ExtractedFileInfo fileInfo = ZipUtils.extractAllFilesToDirectory(fis, extractDir);
            
            // 验证结果
            assertTrue(fileInfo.hasXml());
            assertTrue(fileInfo.hasPdf());
            assertTrue(fileInfo.hasOfd());
            
            // 验证文件是否存在
            assertTrue(Files.exists(Paths.get(fileInfo.getXmlFilePath())));
            assertTrue(Files.exists(Paths.get(fileInfo.getPdfFilePath())));
            assertTrue(Files.exists(Paths.get(fileInfo.getOfdFilePath())));
            
            // 验证文件内容
            String xmlContent = new String(Files.readAllBytes(Paths.get(fileInfo.getXmlFilePath())));
            assertEquals("<root><test>XML Content</test></root>", xmlContent);
            
            System.out.println("XML文件路径: " + fileInfo.getXmlFilePath());
            System.out.println("PDF文件路径: " + fileInfo.getPdfFilePath());
            System.out.println("OFD文件路径: " + fileInfo.getOfdFilePath());
        }
        
        // 清理临时文件
        Files.deleteIfExists(xmlFile);
        Files.deleteIfExists(pdfFile);
        Files.deleteIfExists(ofdFile);
        Files.deleteIfExists(zipFile);
        Files.deleteIfExists(tempDir);
        // 注意：解压目录中的文件会在JVM退出时自动清理
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