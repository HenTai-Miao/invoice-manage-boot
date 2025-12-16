package com.zz.common.error;

/**
 * 自定义业务异常类
 */
public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private int code;
    private String message;
    
    public ServiceException() {
        super();
    }
    
    public ServiceException(String message) {
        super(message);
        this.message = message;
    }
    
    public ServiceException(ResultCode ResultCode) {
        super(ResultCode.getMessage());
        this.code = ResultCode.getCode();
        this.message = ResultCode.getMessage();
    }
    
    public ServiceException(ResultCode ResultCode, String message) {
        super(message);
        this.code = ResultCode.getCode();
        this.message = message;
    }
    
    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
    
    public ServiceException(ResultCode ResultCode, Throwable cause) {
        super(ResultCode.getMessage(), cause);
        this.code = ResultCode.getCode();
        this.message = ResultCode.getMessage();
    }
    
    public ServiceException(Throwable cause) {
        super(cause);
    }
    
    public int getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}