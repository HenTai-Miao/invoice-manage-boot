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
    ;
    private final int code;
    private final String message;
    
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}