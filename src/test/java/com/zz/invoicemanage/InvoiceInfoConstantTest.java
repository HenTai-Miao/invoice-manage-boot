package com.zz.invoicemanage;

import com.zz.common.constant.InvoiceInfoConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InvoiceInfoConstantTest {

    @Test
    public void testGetAllInvoiceInfoConstants() {
        // 获取所有发票信息常量
        List<String> constants = InvoiceInfoConstant.getAllInvoiceInfoConstants();
        
        // 验证列表不为空
        assertNotNull(constants);
        assertFalse(constants.isEmpty());
        
        // 验证包含一些关键常量
        assertTrue(constants.contains(InvoiceInfoConstant.EI_ID));
        assertTrue(constants.contains(InvoiceInfoConstant.ITEM_NAME));
        assertTrue(constants.contains(InvoiceInfoConstant.AMOUNT));
        assertTrue(constants.contains(InvoiceInfoConstant.BUYER_NAME));
        assertTrue(constants.contains(InvoiceInfoConstant.SELLER_NAME));
        assertTrue(constants.contains(InvoiceInfoConstant.REQUEST_TIME));
        assertTrue(constants.contains(InvoiceInfoConstant.DRAWER));
        
        // 打印常量数量
        System.out.println("发票信息常量总数: " + constants.size());
        
        // 打印所有常量
        System.out.println("所有发票信息常量:");
        for (String constant : constants) {
            System.out.println("- " + constant);
        }
    }
}