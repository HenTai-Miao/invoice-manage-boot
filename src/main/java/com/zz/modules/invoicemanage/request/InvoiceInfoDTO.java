package com.zz.modules.invoicemanage.request;

import lombok.Data;

/**
 * 发票信息
 *
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/10
 * Time：16:22
 * ================================
 */
@Data
public class InvoiceInfoDTO {
    /**
     * 发票号码
     */
    private String EIid;

    /**
     * 项目名称
     */
    private String ItemName;

    /**
     * 项目规格
     */
    private String SpecMod;

    /**
     * 单位
     */
    private String MeaUnits;

    /**
     * 数量
     */
    private String Quantity;

    /**
     * 单价
     */
    private String UnPrice;

    /**
     * 金额
     */
    private String Amount;

    /**
     * 收率/征收率
     */
    private String TaxRate;

    /**
     * 税额
     */
    private String ComTaxAm;

    /**
     * 价税合计（大写）TotalTax-includedAmountInChinese
     */
    private String TotalTaxIncludedAmountInChinese;

    /**
     * 价税合计（小写）TotalTax-includedAmount
     */
    private String TotalTaxIncludedAmount;

    /**
     * 购方名称
     */
    private String BuyerName;
    /**
     * 购方统一社会信用代码/纳税人识别号
     */
    private String BuyerIdNum;
    /**
     * 购方开户银行
     */
    private String BuyerBankName;
    /**
     * 购方银行账号
     */
    private String BuyerBankAccNum;
    /**
     * 销方名称
     */
    private String SellerName;
    /**
     * 销方统一社会信用代码/纳税人识别号
     */
    private String SellerIdNum;
    /**
     * 销方地址
     */
    private String SellerAddr;
    /**
     * 销方电话
     */
    private String SellerTelNum;
    /**
     * 销方开户银行
     */
    private String SellerBankName;
    /**
     * 销方银行账号
     */
    private String SellerBankAccNum;

    /**
     * 开票日期
     */
    private String RequestTime;

    /**
     * 开票人
     */
    private String Drawer;
}
