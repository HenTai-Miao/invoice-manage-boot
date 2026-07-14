package com.zz.invoicemanage;

import com.zz.common.error.ZipFilenameEncodingException;
import com.zz.common.util.ZipUtils;
import com.zz.modules.invoicemanage.request.dto.FileGroupInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZipCharsetCompatibilityTest {

    private static final String INVOICE_NUMBER = "12345678901234567890";

    @TempDir
    Path tempDirectory;

    @Test
    void extractsCompleteInvoiceGroup_whenEntryNamesUseGbk() throws Exception {
        // Given
        Path zipPath = tempDirectory.resolve("legacy-gbk.zip");
        createInvoiceZip(zipPath, Charset.forName("GBK"));

        // When
        List<FileGroupInfo> groups;
        try (InputStream inputStream = Files.newInputStream(zipPath)) {
            groups = ZipUtils.extractFileGroupsToDirectory(
                    inputStream,
                    tempDirectory.resolve("extracted"));
        }

        // Then
        assertEquals(1, groups.size());
        FileGroupInfo group = groups.get(0);
        assertTrue(group.hasXml());
        assertTrue(group.hasPdf());
        assertTrue(group.hasOfd());
    }

    @Test
    void reportsSafeError_whenEntryNamesAreNeitherUtf8NorGbk() throws Exception {
        // Given: CP437 encodes 'ÿ' as a byte sequence invalid in both UTF-8 and GBK.
        Path zipPath = tempDirectory.resolve("unsupported-filename-encoding.zip");
        Path extractionDirectory = tempDirectory.resolve("unsupported-extracted");
        try (ZipOutputStream outputStream = new ZipOutputStream(
                Files.newOutputStream(zipPath), Charset.forName("Cp437"))) {
            writeEntry(outputStream, "dzfp_" + INVOICE_NUMBER + "_ÿ.pdf", "pdf");
        }

        // When
        ZipFilenameEncodingException error;
        try (InputStream inputStream = Files.newInputStream(zipPath)) {
            error = assertThrows(
                    ZipFilenameEncodingException.class,
                    () -> ZipUtils.extractFileGroupsToDirectory(
                            inputStream,
                            extractionDirectory));
        }

        // Then
        assertEquals("ZIP文件名编码不兼容（仅支持UTF-8或GBK）", error.getMessage());
        try (java.util.stream.Stream<Path> paths = Files.list(extractionDirectory)) {
            assertEquals(0L, paths.count(), "Failed charset attempt directories must be removed");
        }
    }

    @Test
    void rejectsArchive_whenEntryCountExceedsSafetyLimit() throws Exception {
        // Given
        Path zipPath = tempDirectory.resolve("too-many-entries.zip");
        try (ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            for (int index = 0; index < 2001; index++) {
                writeEntry(outputStream, "entry-" + index + ".txt", "");
            }
        }

        // When
        IOException error;
        try (InputStream inputStream = Files.newInputStream(zipPath)) {
            error = assertThrows(
                    IOException.class,
                    () -> ZipUtils.extractFileGroupsToDirectory(
                            inputStream,
                            tempDirectory.resolve("too-many-entries-extracted")));
        }

        // Then
        assertEquals("ZIP文件包含过多条目", error.getMessage());
    }

    private static void createInvoiceZip(Path zipPath, Charset filenameCharset) throws Exception {
        try (ZipOutputStream outputStream = new ZipOutputStream(
                Files.newOutputStream(zipPath), filenameCharset)) {
            writeEntry(outputStream, invoiceEntryName("pdf"), "pdf");
            writeEntry(outputStream, invoiceEntryName("ofd"), "ofd");
            writeEntry(outputStream, invoiceEntryName("xml"), "<EInvoice/>");
        }
    }

    private static String invoiceEntryName(String extension) {
        return "dzfp_" + INVOICE_NUMBER + "_测试购买方." + extension;
    }

    private static void writeEntry(
            ZipOutputStream outputStream,
            String entryName,
            String content) throws Exception {
        outputStream.putNextEntry(new ZipEntry(entryName));
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.closeEntry();
    }
}
