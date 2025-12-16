package com.zz.invoicemanage;

import com.zz.modules.invoicemanage.request.dto.EInvoiceParser;
import com.zz.common.constant.InvoiceInfoConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InvoiceItemParserTest {

    @Test
    public void testExtractAllItems() throws Exception {
        // 测试提取多个项目信息
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<EInvoice>\n" +
                "  <EIid>1234567890</EIid>\n" +
                "  <BuyerName>测试购买方</BuyerName>\n" +
                "  <SellerName>测试销售方</SellerName>\n" +
                "  <Item>\n" +
                "    <ItemName>商品1</ItemName>\n" +
                "    <ItemSpec>规格1</ItemSpec>\n" +
                "    <Unit>个</Unit>\n" +
                "    <Quantity>10</Quantity>\n" +
                "    <UnitPrice>5.00</UnitPrice>\n" +
                "    <Amount>50.00</Amount>\n" +
                "    <TaxRate>0.13</TaxRate>\n" +
                "    <ComTaxAm>6.50</ComTaxAm>\n" +
                "  </Item>\n" +
                "  <Item>\n" +
                "    <ItemName>商品2</ItemName>\n" +
                "    <ItemSpec>规格2</ItemSpec>\n" +
                "    <Unit>件</Unit>\n" +
                "    <Quantity>5</Quantity>\n" +
                "    <UnitPrice>20.00</UnitPrice>\n" +
                "    <Amount>100.00</Amount>\n" +
                "    <TaxRate>0.13</TaxRate>\n" +
                "    <ComTaxAm>13.00</ComTaxAm>\n" +
                "  </Item>\n" +
                "</EInvoice>";

        // 提取项目信息
        List<Map<String, String>> items = EInvoiceParser.extractAllItems(xmlContent);

        // 验证结果
        assertNotNull(items);
        assertEquals(2, items.size());

        // 验证第一个项目的信息
        Map<String, String> firstItem = items.get(0);
        assertEquals("商品1", firstItem.get(InvoiceInfoConstant.ITEM_NAME));
        assertEquals("10", firstItem.get(InvoiceInfoConstant.QUANTITY));
        assertEquals("50.00", firstItem.get(InvoiceInfoConstant.AMOUNT));
        assertEquals("0.13", firstItem.get(InvoiceInfoConstant.TAX_RATE));
        assertEquals("6.50", firstItem.get(InvoiceInfoConstant.COM_TAX_AM));

        // 验证第二个项目的信息
        Map<String, String> secondItem = items.get(1);
        assertEquals("商品2", secondItem.get(InvoiceInfoConstant.ITEM_NAME));
        assertEquals("5", secondItem.get(InvoiceInfoConstant.QUANTITY));
        assertEquals("100.00", secondItem.get(InvoiceInfoConstant.AMOUNT));
        assertEquals("0.13", secondItem.get(InvoiceInfoConstant.TAX_RATE));
        assertEquals("13.00", secondItem.get(InvoiceInfoConstant.COM_TAX_AM));

        System.out.println("项目信息提取测试完成，共处理了 " + items.size() + " 个项目");
    }

    @Test
    public void testExtractAllItemsWithNoItems() throws Exception {
        // 测试没有项目信息的情况
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<EInvoice>\n" +
                "  <EIid>1234567890</EIid>\n" +
                "  <BuyerName>测试购买方</BuyerName>\n" +
                "  <SellerName>测试销售方</SellerName>\n" +
                "</EInvoice>";

        // 提取项目信息
        List<Map<String, String>> items = EInvoiceParser.extractAllItems(xmlContent);

        // 验证结果 - 应该返回空列表
        assertNotNull(items);
        assertTrue(items.isEmpty());

        System.out.println("无项目信息测试完成");
    }
}