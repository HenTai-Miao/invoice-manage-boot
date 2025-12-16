package com.zz.invoicemanage;

import com.zz.common.error.GlobalExceptionHandler;
import com.zz.common.error.R;
import com.zz.common.error.ResultCode;
import com.zz.common.error.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 全局异常处理器测试类
 */
@SpringBootTest
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleServiceExceptionWithCode() {
        // 测试带有状态码的业务异常
        ServiceException exception = new ServiceException(ResultCode.FILE_UPLOAD_ERROR, "文件上传失败");
        R<Void> result = exceptionHandler.handleServiceException(exception);
        
        assertNotNull(result);
        assertEquals(ResultCode.FILE_UPLOAD_ERROR.getCode(), result.getCode());
        assertEquals("文件上传失败", result.getMessage());
        assertFalse(result.isSuccess());
    }

    @Test
    public void testHandleServiceExceptionWithoutCode() {
        // 测试不带状态码的业务异常
        ServiceException exception = new ServiceException("未知业务异常");
        R<Void> result = exceptionHandler.handleServiceException(exception);
        
        assertNotNull(result);
        assertEquals(ResultCode.PARAM_ERROR.getCode(), result.getCode());
        assertEquals("未知业务异常", result.getMessage());
        assertFalse(result.isSuccess());
    }

    @Test
    public void testHandleSystemException() {
        // 测试系统异常
        Exception exception = new RuntimeException("系统内部错误");
        R<Void> result = exceptionHandler.handleException(exception);
        
        assertNotNull(result);
        assertEquals(ResultCode.SYSTEM_ERROR.getCode(), result.getCode());
        assertEquals(ResultCode.SYSTEM_ERROR.getMessage(), result.getMessage());
        assertFalse(result.isSuccess());
    }
}