package com.example.producer.controller;

import com.example.producer.client.ConsumerServiceClient;
import dto.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consume")
public class ConsumerController {
    private final RestTemplate restTemplate;
    private final ConsumerServiceClient consumerServiceClient;

    public ConsumerController(RestTemplate restTemplate, ConsumerServiceClient consumerServiceClient) {
        this.restTemplate = restTemplate;
        this.consumerServiceClient = consumerServiceClient;
    }

    @GetMapping("/call")
    public String callMyService() {
        String response = restTemplate.getForObject("http://consumer-service/hello/pathVariable/default", String.class);
        return "Response from customer-service: " + response;
    }

    @GetMapping("/callByPathVariable/{name}")
    public String callMyServiceUsingPathVariable(@PathVariable String name) {
        String response = consumerServiceClient.getHelloByPathVariable(name);
        return "Response from customer-service: " + response;
    }

    @GetMapping("/callByRequestParam")
    public String callMyServiceUsingRequestParam(@RequestParam String name) {
        String response = consumerServiceClient.getHelloByRequestParam(name);
        return "Response from customer-service: " + response;
    }

    @PostMapping("/createUser")
    public String callCreateUser(@RequestBody User user) {
        return consumerServiceClient.createUser(user);
    }
}
