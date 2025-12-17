package com.zz.modules.invoicemanage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zz.common.util.PageUtil;
import com.zz.common.util.Query;
import com.zz.modules.invoicemanage.converter.InvoiceInfoConverter;
import com.zz.modules.invoicemanage.entity.InvoiceInfoEntity;
import com.zz.modules.invoicemanage.mapper.InvoiceInfoMapper;
import com.zz.modules.invoicemanage.request.QueryListRequest;
import com.zz.modules.invoicemanage.response.QueryListResponse;
import com.zz.modules.invoicemanage.service.IInvoiceInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IInvoiceInfoServiceImpl extends ServiceImpl<InvoiceInfoMapper, InvoiceInfoEntity> implements IInvoiceInfoService {

    private final InvoiceInfoMapper invoiceInfoMapper;

    @Override
    public IPage<QueryListResponse> queryList(QueryListRequest queryListRequest, Query query) {
        IPage<InvoiceInfoEntity> page = PageUtil.getPage(query);
        IPage<InvoiceInfoEntity> invoiceInfoEntityIPage = invoiceInfoMapper.selectPage(page, Wrappers.<InvoiceInfoEntity>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(queryListRequest.getEiId()), InvoiceInfoEntity::getEiId, queryListRequest.getEiId())
                .like(ObjectUtil.isNotEmpty(queryListRequest.getBuyerName()), InvoiceInfoEntity::getBuyerName, queryListRequest.getBuyerName())
                .like(ObjectUtil.isNotEmpty(queryListRequest.getBuyerIdNum()), InvoiceInfoEntity::getBuyerIdNum, queryListRequest.getBuyerIdNum())
                .like(ObjectUtil.isNotEmpty(queryListRequest.getSellerName()), InvoiceInfoEntity::getSellerName, queryListRequest.getSellerName())
                .like(ObjectUtil.isNotEmpty(queryListRequest.getSellerIdNum()), InvoiceInfoEntity::getSellerIdNum, queryListRequest.getSellerIdNum())
                .eq(ObjectUtil.isNotEmpty(queryListRequest.getBuyerSubjectType()), InvoiceInfoEntity::getBuyerSubjectType, queryListRequest.getBuyerSubjectType())
                .ge(ObjectUtil.isNotEmpty(queryListRequest.getRequestTimeStart()), InvoiceInfoEntity::getRequestTime, queryListRequest.getRequestTimeStart())
                .le(ObjectUtil.isNotEmpty(queryListRequest.getRequestTimeEnd()), InvoiceInfoEntity::getRequestTime, queryListRequest.getRequestTimeEnd())
                .orderByDesc(InvoiceInfoEntity::getCreateTime)
        );
        if (ObjectUtil.isEmpty(invoiceInfoEntityIPage.getRecords())) {
            return new Page<>(query.getCurrent(), query.getSize());
        }
        return invoiceInfoEntityIPage.convert(InvoiceInfoConverter.INSTANCE::toQueryListResponse);
    }

    /**
     * 根据发票号码判断发票是否存在
     *
     * @param eiId 发票号码
     * @return true：存在，false：不存在
     */
    @Override
    public boolean isExists(String eiId) {
        return invoiceInfoMapper.exists(Wrappers.<InvoiceInfoEntity>lambdaQuery().eq(InvoiceInfoEntity::getEiId, eiId));
    }
}