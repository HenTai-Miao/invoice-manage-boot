package com.zz.modules.resource.service;

import com.zz.modules.resource.entity.AttachEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/12
 * Time：02:10
 * ================================
 */
public interface IOssService {

    String invoicePutFile(MultipartFile file);

    void downloadFile(String filePath, HttpServletResponse response);

    AttachEntity detail(Long id);

    AttachEntity detailByName(String fileName);
}
