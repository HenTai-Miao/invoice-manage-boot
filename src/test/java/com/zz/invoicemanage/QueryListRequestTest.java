package com.zz.invoicemanage;

import com.zz.modules.invoicemanage.request.QueryListRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * QueryListRequest测试类
 */
public class QueryListRequestTest {

    @Test
    public void testRequestTimeFields() {
        QueryListRequest request = new QueryListRequest();
        
        // 测试设置和获取开票日期开始时间
        request.setRequestTimeStart("2025-01-01");
        assertEquals("2025-01-01", request.getRequestTimeStart());
        
        // 测试设置和获取开票日期结束时间
        request.setRequestTimeEnd("2025-12-31");
        assertEquals("2025-12-31", request.getRequestTimeEnd());
    }
}