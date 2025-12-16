package com.zz.modules.invoicemanage.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 发票项目信息响应DTO
 *
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/12
 * Time：18:30
 * ================================
 */
@Data
public class InvoiceItemResponse {

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 项目规格
     */
    private String itemSpec;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 收率/征收率
     */
    private BigDecimal taxRate;

    /**
     * 税额
     */
    private BigDecimal comTaxAm;
}