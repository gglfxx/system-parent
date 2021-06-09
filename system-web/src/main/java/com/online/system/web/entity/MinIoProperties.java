package com.online.system.web.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * minio配置
 */
@Data
@ConfigurationProperties(prefix = "minio.io")
public class MinIoProperties {

    private String endpoint;

    private String bucketName;

    private String accessKey;

    private String secretKey;
}
