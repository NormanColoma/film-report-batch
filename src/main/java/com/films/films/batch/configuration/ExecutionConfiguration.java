package com.films.films.batch.configuration;

import com.google.api.client.util.Value;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Data
public class ExecutionConfiguration {
    private String filePath;
    private String credentials;
}
