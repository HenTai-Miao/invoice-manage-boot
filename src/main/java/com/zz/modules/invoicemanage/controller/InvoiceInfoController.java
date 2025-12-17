package com.zz.modules.invoicemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zz.common.constant.AppConstant;
import com.zz.common.error.R;
import com.zz.common.util.Query;
import com.zz.modules.invoicemanage.request.QueryListRequest;
import com.zz.modules.invoicemanage.response.QueryListResponse;
import com.zz.modules.invoicemanage.service.IInvoiceInfoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 发票信息控制器
 *
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/12
 * Time：10:30
 * ================================
 */
@AllArgsConstructor
@RestController
@RequestMapping(AppConstant.APPLICATION_INVOICE_MANAGE_NAME + "/invoice")
public class InvoiceInfoController {

    private final IInvoiceInfoService invoiceInfoService;

    /**
     * 分页查询发票信息
     */
    @GetMapping("/queryList")
    public R<IPage<QueryListResponse>> queryList(@Valid QueryListRequest queryListRequest, @Valid Query query) {
        IPage<QueryListResponse> result = invoiceInfoService.queryList(queryListRequest, query);
        return R.data(result);
    }
}