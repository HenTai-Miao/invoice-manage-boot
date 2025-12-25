package com.zz.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/10
 * Time：16:50
 * ================================
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss.minio")
public class OssMinioConstant {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
