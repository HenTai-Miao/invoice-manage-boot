package com.zz.modules.invoicemanage.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/12
 * Time：16:12
 * ================================
 */
@Data
public class QueryListResponse {

    /**
     * 发票主键
     */
    private Long id;

    /**
     * 发票号码
     */
    private String eiId;

    /**
     * 购买方主体类型
     */
    private String buyerSubjectType;

    /**
     * 电子发票类型
     */
    private String eInvoiceType;

    /**
     * 普通发票或专用发票类型
     */
    private String generalOrSpecialVAT;

    /**
     * 纳税人类型
     */
    private String taxpayerType;

    /**
     * 购买方名称
     */
    private String buyerName;

    /**
     * 购买方统一社会信用代码/纳税人识别号
     */
    private String buyerIdNum;

    /**
     * 购买方开户银行
     */
    private String buyerBankName;

    /**
     * 购买方开户银行卡号
     */
    private String buyerBankAccNum;

    /**
     * 销售方名称
     */
    private String sellerName;

    /**
     * 销售方统一社会信用代码/纳税人识别号
     */
    private String sellerIdNum;

    /**
     * 销售方地址
     */
    private String sellerAddr;

    /**
     * 销售方电话号码
     */
    private String sellerTelNum;

    /**
     * 销售方开户银行
     */
    private String sellerBankName;

    /**
     * 销售方开户银行卡号
     */
    private String sellerBankAccNum;

    /**
     * XML文件ID
     */
    private String xmlFilePath;

    /**
     * PDF文件ID
     */
    private String pdfFilePath;

    /**
     * OFD文件ID
     */
    private String ofdFilePath;

    /**
     * 请求时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;

    /**
     * 金额合计
     */
    private BigDecimal totalAmWithoutTax;

    /**
     * 税额合计
     */
    private BigDecimal totalTaxAm;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发行项目信息
     */
    private String issuItemInformation;
}