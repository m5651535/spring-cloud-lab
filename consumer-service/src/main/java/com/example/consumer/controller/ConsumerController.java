package com.example.consumer.controller;

import com.example.consumer.client.MyServiceClient;
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
    private final MyServiceClient myServiceClient;

    public ConsumerController(RestTemplate restTemplate, MyServiceClient myServiceClient) {
        this.restTemplate = restTemplate;
        this.myServiceClient = myServiceClient;
    }

    @GetMapping("/call")
    public String callMyService() {
        String response = restTemplate.getForObject("http://my-service/hello/pathVariable/default", String.class);
        return "Response from customer-service: " + response;
    }

    @GetMapping("/callByPathVariable/{name}")
    public String callMyServiceUsingPathVariable(@PathVariable String name) {
        String response = myServiceClient.getHelloByPathVariable(name);
        return "Response from customer-service: " + response;
    }

    @GetMapping("/callByRequestParam")
    public String callMyServiceUsingRequestParam(@RequestParam String name) {
        String response = myServiceClient.getHelloByRequestParam(name);
        return "Response from customer-service: " + response;
    }

    @PostMapping("/createUser")
    public String callCreateUser(@RequestBody User user) {
        return myServiceClient.createUser(user);
    }
}
