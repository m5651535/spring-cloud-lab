package com.example.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@LoadBalancerClient(name = "producer-service")
public class ProducerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProducerServiceApplication.class, args);
	}
}