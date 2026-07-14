package com.zz.invoicemanage;

import com.zz.common.util.ZipUtils;
import com.zz.modules.invoicemanage.converter.InvoiceInfoConverter;
import com.zz.modules.invoicemanage.request.dto.EInvoiceParser;
import com.zz.modules.invoicemanage.request.dto.FileGroupInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class InvoiceZipRegressionTest {

    private static final String ZIP_PATH_PROPERTY = "invoice.test.zip";

    @TempDir
    Path extractionDirectory;

    @Test
    void parsesEveryInvoice_whenUsingProductionFailedZip() throws Exception {
        // Given
        String configuredPath = System.getProperty(ZIP_PATH_PROPERTY);
        assumeTrue(configuredPath != null && !configuredPath.trim().isEmpty(),
                () -> "Run with -D" + ZIP_PATH_PROPERTY + "=/path/to/invoice.zip");
        Path zipPath = Paths.get(configuredPath);
        assertTrue(Files.isRegularFile(zipPath), () -> "ZIP does not exist: " + zipPath);

        // When
        List<FileGroupInfo> fileGroups;
        try (InputStream inputStream = Files.newInputStream(zipPath)) {
            fileGroups = assertDoesNotThrow(
                    () -> ZipUtils.extractFileGroupsToDirectory(inputStream, extractionDirectory),
                    "ZIP extraction and grouping must succeed");
        }

        // Then
        assertFalse(fileGroups.isEmpty(), "ZIP must contain at least one complete invoice group");
        assertEquals(70, fileGroups.size(), "Production regression ZIP must contain all 70 invoice groups");
        for (FileGroupInfo fileGroup : fileGroups) {
            assertDoesNotThrow(
                    () -> parseLocally(fileGroup),
                    () -> "Local parsing failed for invoice group: " + fileGroup.getBaseName());
        }
    }

    private static void parseLocally(FileGroupInfo fileGroup) throws Exception {
        String xmlContent = new String(
                Files.readAllBytes(Paths.get(fileGroup.getXmlFilePath())),
                StandardCharsets.UTF_8);
        Map<String, String> tags = EInvoiceParser.extractMultipleTags(xmlContent);
        EInvoiceParser.extractAllItems(xmlContent);
        EInvoiceParser.parseInvoiceEnums(xmlContent);
        InvoiceInfoConverter.convertToEntity(tags);
    }
}
