package com.example.consumer.controller;

import com.example.consumer.client.MyServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consume")
public class ConsumerController {
    private final RestTemplate restTemplate;
    private final MyServiceClient myServiceClient;

    public ConsumerController(RestTemplate restTemplate, MyServiceClient myServiceClient) {
        this.restTemplate = restTemplate;
        this.myServiceClient = myServiceClient;
    }

    @GetMapping("/call")
    public String callMyService() {
        String response = restTemplate.getForObject("http://my-service/hello", String.class);
        return "Response from customer-service: " + response;
    }

    @GetMapping("/callByClient")
    public String callMyServiceByClient() {
        String response = myServiceClient.getHello();
        return "Response from customer-service: " + response;
    }
}
