package com.films.films.batch.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "google.storage")
@Data
public class GoogleStorageConfiguration {
    private String credentials;
    private String bucket;
    private String projectId;
}
