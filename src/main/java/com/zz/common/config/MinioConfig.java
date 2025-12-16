package com.zz.common.config;

import com.zz.common.constant.OssMinioConstant;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Autowired
    private OssMinioConstant ossMinioConstant;

    @Bean
    public MinioClient minioClient() {
        log.info("Initializing MinIO client with endpoint: {}", ossMinioConstant.getEndpoint());
        return MinioClient.builder()
                .endpoint(ossMinioConstant.getEndpoint())
                .credentials(ossMinioConstant.getAccessKey(), ossMinioConstant.getSecretKey())
                .build();
    }
}