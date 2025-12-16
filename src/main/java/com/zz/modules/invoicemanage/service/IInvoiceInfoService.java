package com.zz.modules.invoicemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zz.common.util.Query;
import com.zz.modules.invoicemanage.entity.InvoiceInfoEntity;
import com.zz.modules.invoicemanage.request.QueryListRequest;
import com.zz.modules.invoicemanage.response.QueryListResponse;

import javax.validation.Valid;
import java.util.List;

public interface IInvoiceInfoService extends IService<InvoiceInfoEntity> {
    
    IPage<QueryListResponse> queryList(QueryListRequest queryListRequest, Query query);

    /**
     * 根据发票号码判断发票是否存在
     *
     * @param eiId 发票号码
     * @return true：存在，false：不存在
     */
    boolean isExists(String eiId);
}