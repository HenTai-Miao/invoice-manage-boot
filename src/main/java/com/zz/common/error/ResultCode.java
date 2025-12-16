package com.zz.common.error;

import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
public enum ResultCode {
    // 成功
    SUCCESS(200, "操作成功"),
    
    // 系统错误
    SYSTEM_ERROR(500, "系统错误"),
    PARAM_ERROR(400, "业务异常"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    
    // 业务错误
    FILE_UPLOAD_ERROR(1001, "文件上传失败"),
    FILE_TYPE_ERROR(1002, "文件类型错误"),
    FILE_PROCESS_ERROR(1003, "文件处理失败"),
    XML_PARSE_ERROR(1004, "XML解析失败"),
    INVOICE_SAVE_ERROR(1005, "发票信息保存失败"),
    MINIO_ERROR(1006, "文件存储服务异常"),
    
    // ZIP处理相关错误
    ZIP_FILE_ERROR(2001, "ZIP文件处理失败"),
    ZIP_NO_XML_ERROR(2002, "ZIP文件中缺少XML文件"),
    ZIP_ENTRY_ERROR(2003, "ZIP文件条目处理失败");
    
    private final int code;
    private final String message;
    
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}