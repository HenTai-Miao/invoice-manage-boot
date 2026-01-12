package com.zz.modules.invoicemanage.converter;

import com.zz.common.constant.InvoiceInfoConstant;
import com.zz.modules.invoicemanage.entity.InvoiceInfoEntity;
import com.zz.modules.invoicemanage.response.QueryListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 发票信息转换器
 * 用于将解析的XML标签值转换为InvoiceInfoEntity实体
 */
@Mapper
public interface InvoiceInfoConverter {

    InvoiceInfoConverter INSTANCE = Mappers.getMapper(InvoiceInfoConverter.class);

    /**
     * 将InvoiceInfoEntity实体转换为QueryListResponse对象
     *
     * @param invoiceInfoEntity 实体对象
     * @return QueryListResponse对象
     */
    QueryListResponse toQueryListResponse(InvoiceInfoEntity invoiceInfoEntity);
    
    /**
     * 将解析的XML标签映射转换为InvoiceInfoEntity实体
     *
     * @param tags XML标签映射
     * @return InvoiceInfoEntity实体
     */
    static InvoiceInfoEntity convertToEntity(Map<String, String> tags) {
        InvoiceInfoEntity entity = new InvoiceInfoEntity();
        
        // 使用常量作为键从映射中获取值并设置到实体中
        entity.setEiId(getValue(tags, InvoiceInfoConstant.EI_ID));
        entity.setBuyerName(getValue(tags, InvoiceInfoConstant.BUYER_NAME));
        entity.setBuyerIdNum(getValue(tags, InvoiceInfoConstant.BUYER_ID_NUM));
        entity.setBuyerBankName(getValue(tags, InvoiceInfoConstant.BUYER_BANK_NAME));
        entity.setBuyerBankAccNum(getValue(tags, InvoiceInfoConstant.BUYER_BANK_ACC_NUM));
        entity.setSellerName(getValue(tags, InvoiceInfoConstant.SELLER_NAME));
        entity.setSellerIdNum(getValue(tags, InvoiceInfoConstant.SELLER_ID_NUM));
        entity.setSellerAddr(getValue(tags, InvoiceInfoConstant.SELLER_ADDR));
        entity.setSellerTelNum(getValue(tags, InvoiceInfoConstant.SELLER_TEL_NUM));
        entity.setSellerBankName(getValue(tags, InvoiceInfoConstant.SELLER_BANK_NAME));
        entity.setSellerBankAccNum(getValue(tags, InvoiceInfoConstant.SELLER_BANK_ACC_NUM));
//        entity.setTotalAmWithoutTax(new BigDecimal(getValue(tags, InvoiceInfoConstant.TOTAL_AM_WITHOUT_TAX)));
        entity.setTotalAmWithoutTax(new BigDecimal(getValue(tags, InvoiceInfoConstant.TOTAL_TAX_INCLUDED_AMOUNT)));
        entity.setTotalTaxAm(new BigDecimal(getValue(tags, InvoiceInfoConstant.TOTAL_TAX_AM)));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        entity.setRequestTime(LocalDateTime.parse(getValue(tags, InvoiceInfoConstant.REQUEST_TIME), dateTimeFormatter));
        entity.setRemark(getValue(tags, InvoiceInfoConstant.REMARK));

        // 可以根据需要设置其他字段的默认值
        entity.setStatus("0"); // 默认状态为0（正常）
        // isDeleted字段由MyBatis Plus自动处理，默认为0（未删除）

        return entity;
    }
    
    /**
     * 安全地从映射中获取值，避免NullPointerException
     *
     * @param tags 标签映射
     * @param key 键
     * @return 值或null
     */
    static String getValue(Map<String, String> tags, String key) {
        return tags.get(key);
    }
}