package com.iotd.platdocking.nbiot.ft.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *  当前配置环境
 */
@Configuration
@Getter
public class EnvironmentConfig {

    @Value("${application.environment}")
    private String environment;


}
