package com.zz.invoicemanage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.modules.invoicemanage.request.QueryListRequest;
import com.zz.common.util.Query;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testQueryListEndpoint() throws Exception {
        // 创建分页请求参数
        Query query = new Query();
        query.setCurrent(1);
        query.setSize(10);

        // 发送GET请求到查询列表接口
        mockMvc.perform(get("/zz-invoicemanage/invoice/queryList")
                .param("current", "1")
                .param("size", "10"))
                .andExpect(status().isOk());
    }
}