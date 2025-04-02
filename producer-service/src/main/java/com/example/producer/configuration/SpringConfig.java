package com.example.producer.configuration;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.cloud.consul.config.ConsulPropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public ConsulPropertySourceLocator consulPropertySourceLocator(ConsulClient consulClient, ConsulConfigProperties properties) {
        return new ConsulPropertySourceLocator(consulClient, properties);
    }
}
