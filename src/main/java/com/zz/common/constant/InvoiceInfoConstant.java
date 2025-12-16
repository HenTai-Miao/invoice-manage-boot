package com.zz.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/10
 * Time：16:38
 * ================================
 */
public interface InvoiceInfoConstant {

    /**
     * 发票号码
     */
    String EI_ID = "EIid";

    /**
     * 电子发票类型
     */
    String E_INVOICE_TYPE = "EInvoiceType";

    /**
     * 普通发票或专用发票类型
     */
    String GENERAL_OR_SPECIAL_V_A_T = "GeneralOrSpecialVAT";

    /**
     * 纳税人类型
     */
    String TAXPAYER_TYPE = "TaxpayerType";

    /**
     * 价税合计（大写）TotalTax-includedAmountInChinese
     */
    String TOTAL_TAX_INCLUDED_AMOUNT_IN_CHINESE = "TotalTax-includedAmountInChinese";

    /**
     * 价税合计（小写）TotalTax-includedAmount
     */
    String TOTAL_TAX_INCLUDED_AMOUNT = "TotalTax-includedAmount";

    /**
     * 购方名称
     */
    String BUYER_NAME = "BuyerName";
    /**
     * 购方统一社会信用代码/纳税人识别号
     */
    String BUYER_ID_NUM = "BuyerIdNum";
    /**
     * 购方开户银行
     */
    String BUYER_BANK_NAME = "BuyerBankName";
    /**
     * 购方银行账号
     */
    String BUYER_BANK_ACC_NUM = "BuyerBankAccNum";
    /**
     * 销方名称
     */
    String SELLER_NAME = "SellerName";
    /**
     * 销方统一社会信用代码/纳税人识别号
     */
    String SELLER_ID_NUM = "SellerIdNum";
    /**
     * 销方地址
     */
    String SELLER_ADDR = "SellerAddr";
    /**
     * 销方电话
     */
    String SELLER_TEL_NUM = "SellerTelNum";
    /**
     * 销方开户银行
     */
    String SELLER_BANK_NAME = "SellerBankName";
    /**
     * 销方银行账号
     */
    String SELLER_BANK_ACC_NUM = "SellerBankAccNum";

    /**
     * 开票日期
     */
    String REQUEST_TIME = "RequestTime";

    /**
     * 开票人
     */
    String DRAWER = "Drawer";

    /**
     * 金额合计
     */
    String TOTAL_AM_WITHOUT_TAX = "TotalAmWithoutTax";

    /**
     * 税额合计
     */
    String TOTAL_TAX_AM = "TotalTaxAm";

    /**
     * 备注
     */
    String REMARK = "Remark";

    /**
     * 获取所有发票信息常量的列表
     *
     * @return 包含所有发票信息常量的列表
     */
    static List<String> getAllInvoiceInfoConstants() {
        return Arrays.asList(EI_ID, TOTAL_TAX_INCLUDED_AMOUNT_IN_CHINESE, TOTAL_TAX_INCLUDED_AMOUNT, BUYER_NAME, BUYER_ID_NUM,
                BUYER_BANK_NAME, BUYER_BANK_ACC_NUM, SELLER_NAME, SELLER_ID_NUM, SELLER_ADDR, SELLER_TEL_NUM, SELLER_BANK_NAME,
                SELLER_BANK_ACC_NUM, REQUEST_TIME, DRAWER, TOTAL_AM_WITHOUT_TAX, TOTAL_TAX_AM, REMARK);
    }


    /**
     * 项目名称
     */
    String ITEM_NAME = "ItemName";

    /**
     * 项目规格
     */
    String SPEC_MOD = "SpecMod";

    /**
     * 单位
     */
    String MEA_UNITS = "MeaUnits";

    /**
     * 数量
     */
    String QUANTITY = "Quantity";

    /**
     * 单价
     */
    String UN_PRICE = "UnPrice";

    /**
     * 金额
     */
    String AMOUNT = "Amount";

    /**
     * 收率/征收率
     */
    String TAX_RATE = "TaxRate";

    /**
     * 税额
     */
    String COM_TAX_AM = "ComTaxAm";

    static List<String> getIssuItemInformation() {
        return Arrays.asList(ITEM_NAME, SPEC_MOD, MEA_UNITS, QUANTITY, UN_PRICE, AMOUNT, TAX_RATE, COM_TAX_AM);
    }
}