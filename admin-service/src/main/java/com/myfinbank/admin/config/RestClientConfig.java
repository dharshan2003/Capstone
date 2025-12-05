package com.myfinbank.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Customer-service (port 8081)
    @Bean
    public RestClient customerRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    // Account endpoints are also in customer-service
    @Bean
    public RestClient accountRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    // Loan endpoints in customer-service
    @Bean
    public RestClient loanRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    // Zero-balance alert endpoints in customer-service
    @Bean
    public RestClient alertRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
}
