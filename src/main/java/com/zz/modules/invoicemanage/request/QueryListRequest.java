package com.zz.modules.invoicemanage.request;

import lombok.Data;

/**
 * 分页请求
 */
@Data
public class QueryListRequest {

    /**
     * 发票号码
     */
    private String eiId;

    /**
     * 购买方名称
     */
    private String buyerName;

    /**
     * 购买方统一社会信用代码/纳税人识别号
     */
    private String buyerIdNum;


    /**
     * 销售方名称
     */
    private String sellerName;

    /**
     * 销售方统一社会信用代码/纳税人识别号
     */
    private String sellerIdNum;

    /**
     * 购买方主体类型
     */
    private String buyerSubjectType;
    
    /**
     * 开票日期开始时间
     */
    private String requestTimeStart;
    
    /**
     * 开票日期结束时间
     */
    private String requestTimeEnd;
    
}