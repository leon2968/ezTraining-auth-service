package com.eztraining.authservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * configure webClient for different microservices
 */
@Configuration
public class WebClientConfig {
    @Bean
    @Qualifier("userClient")
    @Description("user service client")
    public WebClient userClient(@Value("${application.user-service.url}") String serviceUrl){

        return WebClient.builder()
                .baseUrl(serviceUrl)
                .build() ;
    }
}
