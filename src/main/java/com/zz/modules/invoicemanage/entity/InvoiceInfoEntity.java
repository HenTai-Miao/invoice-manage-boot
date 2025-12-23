package com.zz.modules.invoicemanage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 发票信息实体
 *
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/10
 * Time：18:43
 * ================================
 */
@Data
@TableName("tb_invoice_info")
public class InvoiceInfoEntity {

    /**
     * 主键
     */
    @TableId(
            value = "id",
            type = IdType.ASSIGN_ID
    )
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
    private String generalOrSpecialVat;

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
     * xml文件 minio路径
     */
    private String xmlFilePath;

    /**
     * pdf文件 minio路径
     */
    private String pdfFilePath;

    /**
     * ofd文件 minio路径
     */
    private String ofdFilePath;

    /**
     * 金额合计
     */
    private BigDecimal totalAmWithoutTax;

    /**
     * 税额合计
     */
    private BigDecimal totalTaxAm;

    /**
     * 开票日期
     */
    private LocalDateTime requestTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发行项目信息
     */
    private String issuItemInformation;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建部门
     */
    private String createDept;

    /**
     * 创建时间
     */
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(
            fill = FieldFill.INSERT
    )
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(
            fill = FieldFill.INSERT_UPDATE
    )
    private LocalDateTime updateTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 逻辑删除标识(0:未删除 1:已删除)
     */
    @TableLogic
    @TableField(
            fill = FieldFill.INSERT
    )
    private Integer isDeleted;


}