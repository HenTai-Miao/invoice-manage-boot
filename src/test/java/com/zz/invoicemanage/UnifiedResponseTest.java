package com.zz.invoicemanage;

import com.zz.common.error.R;
import com.zz.common.error.ResultCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UnifiedResponseTest {

    @Test
    public void testSuccessResponse() {
        // 测试成功响应（无数据）
        R<Void> successResult = R.success();
        assertTrue(successResult.isSuccess());
        assertEquals(ResultCode.SUCCESS.getCode(), successResult.getCode());
        assertEquals(ResultCode.SUCCESS.getMessage(), successResult.getMessage());
        assertNull(successResult.getData());
        
        // 测试成功响应（有数据）
        String testData = "test data";
        R<String> successWithData = R.success(testData);
        assertTrue(successWithData.isSuccess());
        assertEquals(ResultCode.SUCCESS.getCode(), successWithData.getCode());
        assertEquals(ResultCode.SUCCESS.getMessage(), successWithData.getMessage());
        assertEquals(testData, successWithData.getData());
        
        // 测试成功响应（自定义消息）
        String customMessage = "操作成功";
        R<Void> successWithMessage = R.successWithMessage(customMessage);
        assertTrue(successWithMessage.isSuccess());
        assertEquals(ResultCode.SUCCESS.getCode(), successWithMessage.getCode());
        assertEquals(customMessage, successWithMessage.getMessage());
        assertNull(successWithMessage.getData());
        
        System.out.println("成功响应测试通过");
    }
    
    @Test
    public void testFailureResponse() {
        // 测试失败响应（使用状态码枚举）
        R<Void> failureResult = R.failure(ResultCode.FILE_TYPE_ERROR);
        assertFalse(failureResult.isSuccess());
        assertEquals(ResultCode.FILE_TYPE_ERROR.getCode(), failureResult.getCode());
        assertEquals(ResultCode.FILE_TYPE_ERROR.getMessage(), failureResult.getMessage());
        
        // 测试失败响应（使用状态码枚举和自定义消息）
        String customMessage = "文件格式不正确";
        R<Void> failureWithCustomMessage = R.failure(ResultCode.FILE_TYPE_ERROR, customMessage);
        assertFalse(failureWithCustomMessage.isSuccess());
        assertEquals(ResultCode.FILE_TYPE_ERROR.getCode(), failureWithCustomMessage.getCode());
        assertEquals(customMessage, failureWithCustomMessage.getMessage());
        
        // 测试失败响应（自定义状态码和消息）
        int customCode = 9999;
        String customMsg = "自定义错误";
        R<Void> failureWithCustomCode = R.failure(customCode, customMsg);
        assertFalse(failureWithCustomCode.isSuccess());
        assertEquals(customCode, failureWithCustomCode.getCode());
        assertEquals(customMsg, failureWithCustomCode.getMessage());
        
        System.out.println("失败响应测试通过");
    }
}