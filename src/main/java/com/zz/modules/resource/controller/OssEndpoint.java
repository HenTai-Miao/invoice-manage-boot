package com.zz.modules.resource.controller;

import com.zz.common.constant.AppConstant;
import com.zz.common.error.R;
import com.zz.modules.resource.entity.AttachEntity;
import com.zz.modules.resource.service.IOssService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/10
 * Time：15:37
 * ================================
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(AppConstant.APPLICATION_RESOURCE_NAME + "/oss/endpoint")
public class OssEndpoint {

    private final IOssService ossService;

    @SneakyThrows
    @PostMapping("/invoice-put-file")
    public R<Void> invoicePutFile(MultipartFile file) {
        ossService.invoicePutFile(file);
        return R.success();
    }

    
    /**
     * 下载文件接口
     * @param filePath 文件key
     */
    @GetMapping("/download")
    public void downloadFile(@RequestParam String filePath, HttpServletResponse response) {
        ossService.downloadFile(filePath, response);
    }

    /**
     * 获取文件详情
     */
    @GetMapping("/detail")
    public R<AttachEntity> detail(@RequestParam Long id) {
        return R.data(ossService.detail(id));
    }

    /**
     * 获取文件详情
     */
    @GetMapping("/detail-by-name")
    public R<AttachEntity> detailByName(@RequestParam String fileName) {
        return R.data(ossService.detailByName(fileName));
    }
}