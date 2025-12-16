package com.zz.common.error;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结果封装类
 * @param <T> 数据类型
 */
@Data
public class R<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private long timestamp;
    
    public R() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public R(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public R(int code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 成功响应（无数据）
     * @param <T> 数据类型
     * @return R对象
     */
    public static <T> R<T> success() {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }
    
    /**
     * 成功响应（有数据）
     * @param data 数据
     * @param <T> 数据类型
     * @return R对象
     */
    public static <T> R<T> success(T data) {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功响应（自定义消息）
     * @param message 响应消息
     * @param <T> 数据类型
     * @return R对象
     */
    public static <T> R<T> successWithMessage(String message) {
        return new R<>(ResultCode.SUCCESS.getCode(), message);
    }
    
    /**
     * 成功响应（自定义消息和数据）
     * @param message 响应消息
     * @param data 数据
     * @param <T> 数据类型
     * @return R对象
     */
    public static <T> R<T> successWithMessage(String message, T data) {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 失败响应（使用状态码枚举）
     * @param resultCode 状态码枚举
     * @param <T> 数据类型
     * @return R对象
     */
    public static <T> R<T> failure(ResultCode resultCode) {
        return new R<>(resultCode.getCode(), resultCode.getMessage());
    }
    
    /**
     * 失败响应（使用状态码枚举和自定义消息）
     * @param resultCode 状态码枚举
     * @param message 自定义消息
     * @param <T> 数据类型
     * @return R对象
     */
    public static <T> R<T> failure(ResultCode resultCode, String message) {
        return new R<>(resultCode.getCode(), message);
    }
    
    /**
     * 失败响应（自定义状态码和消息）
     * @param code 状态码
     * @param message 消息
     * @param <T> 数据类型
     * @return R对象
     */
    public static <T> R<T> failure(int code, String message) {
        return new R<>(code, message);
    }
    
    /**
     * 判断是否成功
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode() == this.code;
    }
    
    /**
     * 返回数据
     * @param data 数据
     * @param <T> 数据类型
     * @return R对象
     */
    public static <T> R<T> data(T data) {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
}