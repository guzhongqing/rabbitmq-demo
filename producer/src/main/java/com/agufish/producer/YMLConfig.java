package com.agufish.producer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class YMLConfig {
    private String host;
    private String port;
    private String username;
    private String password;
}