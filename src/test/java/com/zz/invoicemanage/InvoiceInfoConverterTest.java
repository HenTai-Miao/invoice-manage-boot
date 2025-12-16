package com.zz.invoicemanage;

import com.zz.common.constant.InvoiceInfoConstant;
import com.zz.modules.invoicemanage.converter.InvoiceInfoConverter;
import com.zz.modules.invoicemanage.entity.InvoiceInfoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InvoiceInfoConverterTest {

    @Test
    public void testConvertToEntity() {
        // 创建测试数据
        Map<String, String> tags = new HashMap<>();
        tags.put(InvoiceInfoConstant.EI_ID, "1234567890");
        tags.put(InvoiceInfoConstant.BUYER_NAME, "测试购买方");
        tags.put(InvoiceInfoConstant.BUYER_ID_NUM, "912345678901234567");
        tags.put(InvoiceInfoConstant.BUYER_BANK_NAME, "中国银行");
        tags.put(InvoiceInfoConstant.BUYER_BANK_ACC_NUM, "1234567890123456");
        tags.put(InvoiceInfoConstant.SELLER_NAME, "测试销售方");
        tags.put(InvoiceInfoConstant.SELLER_ID_NUM, "987654321098765432");
        tags.put(InvoiceInfoConstant.SELLER_ADDR, "北京市朝阳区测试街道");
        tags.put(InvoiceInfoConstant.SELLER_TEL_NUM, "010-12345678");
        tags.put(InvoiceInfoConstant.SELLER_BANK_NAME, "工商银行");
        tags.put(InvoiceInfoConstant.SELLER_BANK_ACC_NUM, "9876543210987654");
        tags.put(InvoiceInfoConstant.REQUEST_TIME, "2025-12-10 10:30:00");
        
        // 执行转换
        InvoiceInfoEntity entity = InvoiceInfoConverter.convertToEntity(tags);
        
        // 验证结果
        assertNotNull(entity);
        assertEquals("1234567890", entity.getEiId());
        assertEquals("测试购买方", entity.getBuyerName());
        assertEquals("912345678901234567", entity.getBuyerIdNum());
        assertEquals("中国银行", entity.getBuyerBankName());
        assertEquals("1234567890123456", entity.getBuyerBankAccNum());
        assertEquals("测试销售方", entity.getSellerName());
        assertEquals("987654321098765432", entity.getSellerIdNum());
        assertEquals("北京市朝阳区测试街道", entity.getSellerAddr());
        assertEquals("010-12345678", entity.getSellerTelNum());
        assertEquals("工商银行", entity.getSellerBankName());
        assertEquals("9876543210987654", entity.getSellerBankAccNum());
        assertEquals("2025-12-10 10:30:00", entity.getRequestTime());
        assertEquals("0", entity.getStatus());
        // Note: isDeleted is handled by MyBatis Plus automatically, so we don't set it in the converter
        // assertEquals("0", entity.getIsDeleted());
        
        System.out.println("转换成功!");
        System.out.println("发票号码: " + entity.getEiId());
        System.out.println("购方名称: " + entity.getBuyerName());
        System.out.println("销方名称: " + entity.getSellerName());
    }
    
    @Test
    public void testConvertToEntityWithMissingFields() {
        // 创建测试数据，部分字段缺失
        Map<String, String> tags = new HashMap<>();
        tags.put(InvoiceInfoConstant.EI_ID, "1234567890");
        tags.put(InvoiceInfoConstant.BUYER_NAME, "测试购买方");
        // 故意不设置其他字段
        
        // 执行转换
        InvoiceInfoEntity entity = InvoiceInfoConverter.convertToEntity(tags);
        
        // 验证结果
        assertNotNull(entity);
        assertEquals("1234567890", entity.getEiId());
        assertEquals("测试购买方", entity.getBuyerName());
        assertNull(entity.getBuyerIdNum()); // 应该为null
        assertNull(entity.getSellerName()); // 应该为null
        
        System.out.println("部分字段缺失情况下的转换成功!");
    }
}