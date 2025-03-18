package com.example.myService.controller;

import dto.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class MyServiceController {
    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/pathVariable/{name}")
    public String sayHelloByPathVariable(@PathVariable String name) {
        return "Hello " + name + "! from my-service! port " + serverPort;
    }

    @GetMapping("/requestParam")
    public String sayHelloByRequestParam(@RequestParam String name) {
        return "Hello " + name + "! from my-service! port " + serverPort;
    }

    @PostMapping("/createUser")
    public String createUser(@RequestBody User user) {
        return "create user : " + user.getName() + " success !";
    }
}
