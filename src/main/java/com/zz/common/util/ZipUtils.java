package com.zz.common.util;

import cn.hutool.core.util.ObjectUtil;
import com.zz.common.error.ZipFilenameEncodingException;
import com.zz.modules.invoicemanage.request.dto.ExtractedFileInfo;
import com.zz.modules.invoicemanage.request.dto.FileGroupInfo;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * ZIP文件处理工具类
 * 用于解压包含XML文件的ZIP包
 */
public class ZipUtils {

    private static final int MAX_ZIP_ENTRIES = 2000;
    private static final long MAX_ENTRY_UNCOMPRESSED_BYTES = 100L * 1024 * 1024;
    private static final long MAX_TOTAL_UNCOMPRESSED_BYTES = 200L * 1024 * 1024;

    /**
     * 解压ZIP文件并返回其中的XML文件内容
     * 
     * @param zipFilePath ZIP文件路径
     * @return XML文件内容
     * @throws IOException IO异常
     */
    public static String extractXmlFromZip(String zipFilePath) throws IOException {
        return extractXmlFromZip(Paths.get(zipFilePath));
    }

    /**
     * 解压ZIP文件并返回其中的XML文件内容
     * 
     * @param zipFilePath ZIP文件路径
     * @return XML文件内容
     * @throws IOException IO异常
     */
    public static String extractXmlFromZip(Path zipFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(zipFilePath.toFile())) {
            return extractXmlFromZip(fis);
        }
    }

    /**
     * 解压ZIP文件并返回其中的XML文件内容
     * 
     * @param inputStream ZIP文件输入流
     * @return XML文件内容
     * @throws IOException IO异常
     */
    public static String extractXmlFromZip(InputStream inputStream) throws IOException {
        // 将输入流转换为字节数组，以便可以多次使用
        byte[] zipData = inputStreamToByteArray(inputStream);
        
        // 尝试多种编码方式，按优先级排序
        Charset[] charsetsToTry = {
            StandardCharsets.UTF_8,
            Charset.forName("GBK"),
            Charset.forName("GB2312"),
            Charset.defaultCharset()
        };
        
        for (Charset charset : charsetsToTry) {
            try {
                String result = extractXmlFromZipWithCharset(new ByteArrayInputStream(zipData), charset);
                // 如果成功处理且有结果，返回结果
                if (result != null && !result.isEmpty()) {
                    return result;
                }
            } catch (Exception e) {
                // 记录日志但继续尝试下一种编码
                System.err.println("尝试使用编码 " + charset.name() + " 处理ZIP文件失败: " + e.getMessage());
                // 如果是最后一种编码，重新抛出异常
                if (charset == charsetsToTry[charsetsToTry.length - 1]) {
                    throw new IOException("无法使用任何支持的编码处理ZIP文件", e);
                }
            }
        }
        
        throw new IOException("无法使用任何支持的编码处理ZIP文件");
    }
    
    /**
     * 使用指定字符集解压ZIP文件并返回其中的XML文件内容
     * 
     * @param inputStream ZIP文件输入流
     * @param charset 字符集
     * @return XML文件内容
     * @throws IOException IO异常
     */
    private static String extractXmlFromZipWithCharset(InputStream inputStream, Charset charset) throws IOException {
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(inputStream, charset);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // 安全地获取条目名称
                String entryName = getSafeEntryName(entry, charset);
                if (entryName == null) {
                    zis.closeEntry();
                    continue;
                }
                
                if (entryName.toLowerCase().endsWith(".xml")) {
                    // 找到XML文件，读取其内容
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    return baos.toString("UTF-8");
                }
                zis.closeEntry();
            }
        } catch (IllegalArgumentException e) {
            // 特别处理MALFORMED异常
            if (e.getMessage() != null && e.getMessage().contains("MALFORMED")) {
                throw e; // 重新抛出异常，让上层处理
            }
            throw e;
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    // 忽略关闭异常
                }
            }
        }
        throw new IOException("ZIP文件中未找到XML文件");
    }

    /**
     * 解压ZIP文件并将所有文件保存到指定目录
     * 
     * @param zipFilePath ZIP文件路径
     * @param destDirectory 目标目录
     * @return XML文件的路径
     * @throws IOException IO异常
     */
    public static String extractZipToDirectory(String zipFilePath, String destDirectory) throws IOException {
        return extractZipToDirectory(Paths.get(zipFilePath), Paths.get(destDirectory));
    }

    /**
     * 解压ZIP文件并将所有文件保存到指定目录
     * 
     * @param zipFilePath ZIP文件路径
     * @param destDirectory 目标目录
     * @return XML文件的路径
     * @throws IOException IO异常
     */
    public static String extractZipToDirectory(Path zipFilePath, Path destDirectory) throws IOException {
        // 将文件转换为字节数组，以便可以多次使用
        byte[] zipData = Files.readAllBytes(zipFilePath);
        
        // 尝试多种编码方式，按优先级排序
        Charset[] charsetsToTry = {
            StandardCharsets.UTF_8,
            Charset.forName("GBK"),
            Charset.forName("GB2312"),
            Charset.defaultCharset()
        };
        
        for (Charset charset : charsetsToTry) {
            try {
                String result = extractZipToDirectoryWithCharset(new ByteArrayInputStream(zipData), destDirectory, charset);
                // 如果成功处理且有结果，返回结果
                if (result != null && !result.isEmpty()) {
                    return result;
                }
            } catch (Exception e) {
                // 记录日志但继续尝试下一种编码
                System.err.println("尝试使用编码 " + charset.name() + " 处理ZIP文件失败: " + e.getMessage());
                // 如果是最后一种编码，重新抛出异常
                if (charset == charsetsToTry[charsetsToTry.length - 1]) {
                    throw new IOException("无法使用任何支持的编码处理ZIP文件", e);
                }
            }
        }
        
        throw new IOException("无法使用任何支持的编码处理ZIP文件");
    }
    
    /**
     * 使用指定字符集解压ZIP文件并将所有文件保存到指定目录
     * 
     * @param inputStream ZIP文件输入流
     * @param destDirectory 目标目录
     * @param charset 字符集
     * @return XML文件的路径
     * @throws IOException IO异常
     */
    private static String extractZipToDirectoryWithCharset(InputStream inputStream, Path destDirectory, Charset charset) throws IOException {
        // 创建目标目录
        if (!Files.exists(destDirectory)) {
            Files.createDirectories(destDirectory);
        }

        String xmlFilePath = null;
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(inputStream, charset);
            
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // 安全地获取条目名称
                String entryName = getSafeEntryName(entry, charset);
                if (entryName == null) {
                    zis.closeEntry();
                    continue;
                }
                
                Path filePath = destDirectory.resolve(entryName);
                
                // 防止路径遍历漏洞
                if (!filePath.normalize().startsWith(destDirectory.normalize())) {
                    throw new IOException("ZIP条目在目标目录之外: " + entryName);
                }
                
                if (entry.isDirectory()) {
                    // 创建目录
                    Files.createDirectories(filePath);
                } else {
                    // 创建父目录
                    Path parent = filePath.getParent();
                    if (parent != null && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                    
                    // 写入文件
                    try (OutputStream os = Files.newOutputStream(filePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                    
                    // 检查是否为XML文件
                    if (entryName.toLowerCase().endsWith(".xml")) {
                        xmlFilePath = filePath.toString();
                    }
                }
                zis.closeEntry();
            }
        } catch (IllegalArgumentException e) {
            // 特别处理MALFORMED异常
            if (e.getMessage() != null && e.getMessage().contains("MALFORMED")) {
                throw e; // 重新抛出异常，让上层处理
            }
            throw e;
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    // 忽略关闭异常
                }
            }
        }
        
        if (xmlFilePath == null) {
            throw new IOException("ZIP文件中未找到XML文件");
        }
        
        return xmlFilePath;
    }

    /**
     * 解压ZIP文件并分别返回XML、PDF、OFD文件的路径信息
     * 
     * @param zipFilePath ZIP文件路径
     * @param destDirectory 目标目录
     * @return 包含三种文件路径信息的对象
     * @throws IOException IO异常
     */
    public static ExtractedFileInfo extractAllFilesToDirectory(Path zipFilePath, Path destDirectory) throws IOException {
        // 将文件转换为字节数组，以便可以多次使用
        byte[] zipData = Files.readAllBytes(zipFilePath);
        
        // 尝试使用UTF-8编码，如果失败则使用系统默认编码
        try {
            return extractAllFilesToDirectoryWithCharset(new ByteArrayInputStream(zipData), destDirectory, Charset.forName("UTF-8"));
        } catch (IllegalArgumentException e) {
            // 如果UTF-8编码失败，尝试使用系统默认编码
            return extractAllFilesToDirectoryWithCharset(new ByteArrayInputStream(zipData), destDirectory, Charset.defaultCharset());
        }
    }
    
    /**
     * 使用指定字符集解压ZIP文件并分别返回XML、PDF、OFD文件的路径信息
     * 
     * @param zipFilePath ZIP文件路径
     * @param destDirectory 目标目录
     * @param charset 字符集
     * @return 包含三种文件路径信息的对象
     * @throws IOException IO异常
     */
    private static ExtractedFileInfo extractAllFilesToDirectoryWithCharset(Path zipFilePath, Path destDirectory, Charset charset) throws IOException {
        // 创建目标目录
        if (!Files.exists(destDirectory)) {
            Files.createDirectories(destDirectory);
        }

        ExtractedFileInfo fileInfo = new ExtractedFileInfo();
        
        try (FileInputStream fis = new FileInputStream(zipFilePath.toFile());
             ZipInputStream zis = new ZipInputStream(fis, charset)) {
            
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // 安全地获取条目名称
                String entryName = getSafeEntryName(entry, charset);
                if (entryName == null) {
                    zis.closeEntry();
                    continue;
                }
                
                Path filePath = destDirectory.resolve(entryName);
                
                // 防止路径遍历漏洞
                if (!filePath.normalize().startsWith(destDirectory.normalize())) {
                    throw new IOException("ZIP条目在目标目录之外: " + entryName);
                }
                
                if (entry.isDirectory()) {
                    // 创建目录
                    Files.createDirectories(filePath);
                } else {
                    // 创建父目录
                    Path parent = filePath.getParent();
                    if (parent != null && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                    
                    // 写入文件
                    try (OutputStream os = Files.newOutputStream(filePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                    
                    // 检查文件类型并设置对应路径
                    String fileName = entryName.toLowerCase();
                    if (fileName.endsWith(".xml")) {
                        fileInfo.setXmlFilePath(filePath.toString());
                    } else if (fileName.endsWith(".pdf")) {
                        fileInfo.setPdfFilePath(filePath.toString());
                    } else if (fileName.endsWith(".ofd")) {
                        fileInfo.setOfdFilePath(filePath.toString());
                    }
                }
                zis.closeEntry();
            }
        }
        
        return fileInfo;
    }
    
    /**
     * 解压ZIP文件并分别返回XML、PDF、OFD文件的路径信息
     * 
     * @param inputStream ZIP文件输入流
     * @param destDirectory 目标目录
     * @return 包含三种文件路径信息的对象
     * @throws IOException IO异常
     */
    public static ExtractedFileInfo extractAllFilesToDirectory(InputStream inputStream, Path destDirectory) throws IOException {
        // 将输入流转换为字节数组，以便可以多次使用
        byte[] zipData = inputStreamToByteArray(inputStream);
        
        // 尝试使用UTF-8编码，如果失败则使用系统默认编码
        try {
            return extractAllFilesToDirectoryWithCharset(new ByteArrayInputStream(zipData), destDirectory, Charset.forName("UTF-8"));
        } catch (IllegalArgumentException e) {
            // 如果UTF-8编码失败，尝试使用系统默认编码
            return extractAllFilesToDirectoryWithCharset(new ByteArrayInputStream(zipData), destDirectory, Charset.defaultCharset());
        }
    }
    
    /**
     * 使用指定字符集解压ZIP文件并分别返回XML、PDF、OFD文件的路径信息
     * 
     * @param inputStream ZIP文件输入流
     * @param destDirectory 目标目录
     * @param charset 字符集
     * @return 包含三种文件路径信息的对象
     * @throws IOException IO异常
     */
    private static ExtractedFileInfo extractAllFilesToDirectoryWithCharset(InputStream inputStream, Path destDirectory, Charset charset) throws IOException {
        // 创建目标目录
        if (!Files.exists(destDirectory)) {
            Files.createDirectories(destDirectory);
        }

        ExtractedFileInfo fileInfo = new ExtractedFileInfo();
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(inputStream, charset);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // 安全地获取条目名称
                String entryName = getSafeEntryName(entry, charset);
                if (entryName == null) {
                    zis.closeEntry();
                    continue;
                }
                
                Path filePath = destDirectory.resolve(entryName);
                
                // 防止路径遍历漏洞
                if (!filePath.normalize().startsWith(destDirectory.normalize())) {
                    throw new IOException("ZIP条目在目标目录之外: " + entryName);
                }
                
                if (entry.isDirectory()) {
                    // 创建目录
                    Files.createDirectories(filePath);
                } else {
                    // 创建父目录
                    Path parent = filePath.getParent();
                    if (parent != null && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                    
                    // 写入文件
                    try (OutputStream os = Files.newOutputStream(filePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                    
                    // 检查文件类型并设置对应路径
                    String fileName = entryName.toLowerCase();
                    if (fileName.endsWith(".xml")) {
                        fileInfo.setXmlFilePath(filePath.toString());
                    } else if (fileName.endsWith(".pdf")) {
                        fileInfo.setPdfFilePath(filePath.toString());
                    } else if (fileName.endsWith(".ofd")) {
                        fileInfo.setOfdFilePath(filePath.toString());
                    }
                }
                zis.closeEntry();
            }
        } catch (IllegalArgumentException e) {
            // 特别处理MALFORMED异常
            if (e.getMessage() != null && e.getMessage().contains("MALFORMED")) {
                throw e; // 重新抛出异常，让上层处理
            }
            throw e;
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    // 忽略关闭异常
                }
            }
        }
        
        return fileInfo;
    }
    
    /**
     * 解压ZIP文件并按基础文件名分组返回文件信息
     * 
     * @param inputStream ZIP文件输入流
     * @param destDirectory 目标目录
     * @return 按基础文件名分组的文件信息列表
     * @throws IOException IO异常
     */
    public static List<FileGroupInfo> extractFileGroupsToDirectory(InputStream inputStream, Path destDirectory) throws IOException {
        // 创建目标目录
        if (!Files.exists(destDirectory)) {
            Files.createDirectories(destDirectory);
        }
        
        // 将输入流转换为字节数组，以便可以多次使用
        byte[] zipData = inputStreamToByteArray(inputStream);
        
        // 尝试多种编码方式，按优先级排序
        Charset[] charsetsToTry = {
            StandardCharsets.UTF_8,
            Charset.forName("GBK")
        };
        
        for (int charsetIndex = 0; charsetIndex < charsetsToTry.length; charsetIndex++) {
            Charset charset = charsetsToTry[charsetIndex];
            Path attemptDirectory = Files.createTempDirectory(destDirectory, "zip-charset-");
            try {
                List<FileGroupInfo> fileGroupInfoList = extractFileGroupsWithCharset(
                        new ByteArrayInputStream(zipData), attemptDirectory, charset);
                // 如果成功处理且有结果，返回结果
                return fileGroupInfoList.stream()
                        .filter(item -> ObjectUtil.isNotEmpty(item.getOfdFilePath())
                                && ObjectUtil.isNotEmpty(item.getXmlFilePath())
                                && ObjectUtil.isNotEmpty(item.getPdfFilePath()))
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                if (!isFilenameDecodingError(e)) {
                    throw e;
                }
                deleteDirectoryRecursively(attemptDirectory);
                // 如果是最后一种编码，重新抛出异常
                if (charsetIndex == charsetsToTry.length - 1) {
                    throw new ZipFilenameEncodingException(e);
                }
            }
        }
        
        throw new ZipFilenameEncodingException(
                new IllegalArgumentException("No ZIP filename charset attempts configured"));
    }
    
    /**
     * 使用指定字符集解压ZIP文件并按基础文件名分组返回文件信息
     * 
     * @param inputStream ZIP文件输入流
     * @param destDirectory 目标目录
     * @param charset 字符集
     * @return 按基础文件名分组的文件信息列表
     * @throws IOException IO异常
     */
    private static List<FileGroupInfo> extractFileGroupsWithCharset(InputStream inputStream, Path destDirectory, Charset charset) throws IOException {
        // 创建目标目录
        if (!Files.exists(destDirectory)) {
            Files.createDirectories(destDirectory);
        }
        
        // 用于存储文件信息的Map，key为分组标识
        Map<String, FileGroupInfo> fileGroups = new HashMap<>();
        int entryCount = 0;
        long totalUncompressedBytes = 0L;

        try (ZipInputStream zis = new ZipInputStream(inputStream, charset)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                entryCount++;
                if (entryCount > MAX_ZIP_ENTRIES) {
                    throw new IOException("ZIP文件包含过多条目");
                }
                // 安全地获取条目名称
                String entryName = getSafeEntryName(entry, charset);
                if (entryName == null) {
                    zis.closeEntry();
                    continue;
                }

                Path filePath = destDirectory.resolve(entryName);

                // 防止路径遍历漏洞
                if (!filePath.normalize().startsWith(destDirectory.normalize())) {
                    throw new IOException("ZIP条目在目标目录之外: " + entryName);
                }

                if (entry.isDirectory()) {
                    // 创建目录
                    Files.createDirectories(filePath);
                } else {
                    // 创建父目录
                    Path parent = filePath.getParent();
                    if (parent != null && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }

                    // 写入文件
                    try (OutputStream os = Files.newOutputStream(filePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        long entryUncompressedBytes = 0L;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            entryUncompressedBytes += bytesRead;
                            totalUncompressedBytes += bytesRead;
                            if (entryUncompressedBytes > MAX_ENTRY_UNCOMPRESSED_BYTES ||
                                    totalUncompressedBytes > MAX_TOTAL_UNCOMPRESSED_BYTES) {
                                throw new IOException("ZIP文件解压后过大");
                            }
                            os.write(buffer, 0, bytesRead);
                        }
                    }

                    // 获取分组标识（根据下划线分割，取第二个部分）
                    String groupKey = getGroupKey(entryName);

                    // 获取或创建该分组标识对应的文件组
                    FileGroupInfo fileGroup = fileGroups.computeIfAbsent(groupKey, k -> {
                        FileGroupInfo group = new FileGroupInfo();
                        group.setBaseName(groupKey); // 使用分组标识作为基础名称
                        return group;
                    });

                    // 根据文件扩展名设置对应路径
                    String extension = getFileExtension(entryName).toLowerCase();
                    switch (extension) {
                        case "xml":
                            fileGroup.setXmlFilePath(filePath.toString());
                            // 初始化原始文件名映射并记录XML原始文件名
                            if (fileGroup.getOriginalFileNames() == null) {
                                fileGroup.setOriginalFileNames(new HashMap<>());
                            }
                            fileGroup.getOriginalFileNames().put("xml", entryName);
                            break;
                        case "pdf":
                            fileGroup.setPdfFilePath(filePath.toString());
                            // 记录PDF原始文件名
                            if (fileGroup.getOriginalFileNames() == null) {
                                fileGroup.setOriginalFileNames(new HashMap<>());
                            }
                            fileGroup.getOriginalFileNames().put("pdf", entryName);
                            break;
                        case "ofd":
                            fileGroup.setOfdFilePath(filePath.toString());
                            // 记录OFD原始文件名
                            if (fileGroup.getOriginalFileNames() == null) {
                                fileGroup.setOriginalFileNames(new HashMap<>());
                            }
                            fileGroup.getOriginalFileNames().put("ofd", entryName);
                            break;
                    }
                }
                zis.closeEntry();
            }
        } catch (IllegalArgumentException e) {
            // 特别处理MALFORMED异常
            if (e.getMessage() != null && e.getMessage().contains("MALFORMED")) {
                throw e; // 重新抛出异常，让上层处理
            }
            throw e;
        }
        // 忽略关闭异常

        // 过滤掉无效的文件组（没有XML文件的组）并返回列表
        return fileGroups.values().stream()
                .filter(FileGroupInfo::isValid)
                .collect(Collectors.toList());
    }

    private static boolean isFilenameDecodingError(IllegalArgumentException error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof MalformedInputException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private static void deleteDirectoryRecursively(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException error) throws IOException {
                if (error != null) {
                    throw error;
                }
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    /**
     * 将输入流转换为字节数组
     * 
     * @param inputStream 输入流
     * @return 字节数组
     * @throws IOException IO异常
     */
    private static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    
    /**
     * 根据文件名获取分组标识
     * 按下划线分割，取第二个部分作为分组标识
     * 
     * @param fileName 文件名
     * @return 分组标识
     */
    private static String getGroupKey(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName;
        }
        
        // 获取不带扩展名的文件名
        String baseName = getBaseName(fileName);
        
        // 按下划线分割
        String[] parts = baseName.split("_");
        
        // 如果至少有两个部分，返回第二个部分作为分组标识
        if (parts.length >= 2) {
            return parts[1]; // 第二个部分（索引1）
        }
        
        // 否则返回整个基础文件名
        return baseName;
    }
    
    /**
     * 安全地获取ZIP条目名称
     * 
     * @param entry ZIP条目
     * @param charset 字符集
     * @return 条目名称，如果无法解码则返回null
     */
    private static String getSafeEntryName(ZipEntry entry, Charset charset) {
        try {
            // 直接使用条目的名称，ZipInputStream已经处理了解码
            return entry.getName();
        } catch (IllegalArgumentException e) {
            // 特别处理MALFORMED异常
            if (e.getMessage() != null && e.getMessage().contains("MALFORMED")) {
                System.err.println("ZIP条目名称编码错误(MALFORMED)，使用默认编码尝试: " + e.getMessage());
                // 返回null让调用者跳过这个条目
                return null;
            }
            // 如果出现解码错误，记录日志并跳过该条目
            System.err.println("无法解码ZIP条目名称: " + e.getMessage());
            return null;
        } catch (Exception e) {
            // 如果出现其他解码错误，记录日志并跳过该条目
            System.err.println("无法解码ZIP条目名称: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 获取文件的基础名称（不含扩展名）
     * @param fileName 文件名
     * @return 基础名称
     */
    private static String getBaseName(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }
    
    /**
     * 获取文件扩展名
     * @param fileName 文件名
     * @return 扩展名（小写）
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
