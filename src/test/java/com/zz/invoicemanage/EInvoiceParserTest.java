package com.zz.invoicemanage;

import com.zz.modules.invoicemanage.request.dto.EInvoiceParser;
import com.zz.common.constant.InvoiceInfoConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EInvoiceParserTest {

    @Test
    public void testExtractMultipleTagsWithNonExistent() throws Exception {
        // 测试提取多个标签，其中一些不存在
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<EInvoice>\n" +
                "  <EIid>1234567890</EIid>\n" +
                "  <BuyerName>测试购买方</BuyerName>\n" +
                "  <SellerName>测试销售方</SellerName>\n" +
                "  <NonExistentTag>这个标签不在常量列表中</NonExistentTag>\n" +
                "</EInvoice>";
        
        // 提取存在的和不存在的标签
        Map<String, String> tags = EInvoiceParser.extractMultipleTags(xmlContent);
        
        // 验证结果 - 应该返回所有常量的数量
        assertNotNull(tags);
        assertEquals(InvoiceInfoConstant.getAllInvoiceInfoConstants().size(), tags.size());
        
        // 验证确实提取到了存在的标签
        assertEquals("1234567890", tags.get(InvoiceInfoConstant.EI_ID));
        assertEquals("测试购买方", tags.get(InvoiceInfoConstant.BUYER_NAME));
        assertEquals("测试销售方", tags.get(InvoiceInfoConstant.SELLER_NAME));
        
        // 验证不存在的标签不会影响结果（因为解析器只查找预定义的标签）
        System.out.println("混合标签提取测试完成");
    }
}