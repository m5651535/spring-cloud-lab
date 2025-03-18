package com.example.myService.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyServiceController {
    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from my-service! port " + serverPort;
    }
}
