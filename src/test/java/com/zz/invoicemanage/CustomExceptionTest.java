package com.zz.invoicemanage;

import com.zz.common.error.ServiceException;
import com.zz.common.error.ResultCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomExceptionTest {

    @Test
    public void testBusinessException() {
        // 测试业务异常（使用消息）
        String message = "业务异常测试";
        ServiceException serviceException = new ServiceException(message);
        assertEquals(message, serviceException.getMessage());
        assertNull(serviceException.getCause());
        
        // 测试业务异常（使用状态码枚举）
        ServiceException serviceExceptionWithCode = new ServiceException(ResultCode.FILE_TYPE_ERROR);
        assertEquals(ResultCode.FILE_TYPE_ERROR.getCode(), serviceExceptionWithCode.getCode());
        assertEquals(ResultCode.FILE_TYPE_ERROR.getMessage(), serviceExceptionWithCode.getMessage());
        
        // 测试业务异常（使用状态码枚举和自定义消息）
        String customMessage = "自定义业务异常";
        ServiceException serviceExceptionWithCustomMsg = new ServiceException(ResultCode.FILE_TYPE_ERROR, customMessage);
        assertEquals(ResultCode.FILE_TYPE_ERROR.getCode(), serviceExceptionWithCustomMsg.getCode());
        assertEquals(customMessage, serviceExceptionWithCustomMsg.getMessage());
        
        System.out.println("业务异常测试通过");
    }
    
    @Test
    public void testServiceException() {
        // 测试ZIP处理异常（使用消息）
        String message = "ZIP处理异常测试";
        ServiceException zipException = new ServiceException(message);
        assertEquals(message, zipException.getMessage());
        assertNull(zipException.getCause());
        
        // 测试ZIP处理异常（使用状态码枚举）
        ServiceException zipExceptionWithCode = new ServiceException(ResultCode.ZIP_FILE_ERROR);
        assertEquals(ResultCode.ZIP_FILE_ERROR.getCode(), zipExceptionWithCode.getCode());
        assertEquals(ResultCode.ZIP_FILE_ERROR.getMessage(), zipExceptionWithCode.getMessage());
        
        // 测试ZIP处理异常（使用状态码枚举和自定义消息）
        String customMessage = "自定义ZIP处理异常";
        ServiceException zipExceptionWithCustomMsg = new ServiceException(ResultCode.ZIP_FILE_ERROR, customMessage);
        assertEquals(ResultCode.ZIP_FILE_ERROR.getCode(), zipExceptionWithCustomMsg.getCode());
        assertEquals(customMessage, zipExceptionWithCustomMsg.getMessage());
        
        System.out.println("ZIP处理异常测试通过");
    }
}