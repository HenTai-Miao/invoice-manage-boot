package com.zz.invoicemanage;

import com.zz.common.constant.OssMinioConstant;
import com.zz.common.error.ServiceException;
import com.zz.modules.invoicemanage.service.IInvoiceInfoService;
import com.zz.modules.resource.mapper.AttachMapper;
import com.zz.modules.resource.service.impl.OssServiceImpl;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class OssServiceZipEncodingTest {

    @Test
    void returnsSafeEncodingMessage_whenZipFilenameEncodingIsUnsupported() throws Exception {
        // Given
        OssServiceImpl service = new OssServiceImpl(
                mock(MinioClient.class),
                mock(OssMinioConstant.class),
                mock(IInvoiceInfoService.class),
                mock(AttachMapper.class));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "unsupported.zip",
                "application/zip",
                createUnsupportedZip());

        // When
        ServiceException error = assertThrows(
                ServiceException.class,
                () -> service.invoicePutFile(file));

        // Then
        assertEquals("ZIP文件名编码不兼容（仅支持UTF-8或GBK）", error.getMessage());
    }

    private static byte[] createUnsupportedZip() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ZipOutputStream outputStream = new ZipOutputStream(
                bytes,
                Charset.forName("Cp437"))) {
            outputStream.putNextEntry(new ZipEntry("dzfp_12345678901234567890_ÿ.pdf"));
            outputStream.write("pdf".getBytes(StandardCharsets.UTF_8));
            outputStream.closeEntry();
        }
        return bytes.toByteArray();
    }
}
