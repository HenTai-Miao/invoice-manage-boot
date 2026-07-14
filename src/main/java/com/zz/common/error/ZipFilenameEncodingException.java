package com.zz.common.error;

import java.io.IOException;

/**
 * ZIP entry names cannot be decoded using the filename encodings supported by the service.
 */
public final class ZipFilenameEncodingException extends IOException {

    private static final long serialVersionUID = 1L;

    public ZipFilenameEncodingException(Throwable cause) {
        super("ZIP文件名编码不兼容（仅支持UTF-8或GBK）", cause);
    }
}
