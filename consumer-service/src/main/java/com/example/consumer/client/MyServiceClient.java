package com.example.consumer.client;

import dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "my-service")
public interface MyServiceClient {
    @GetMapping("/hello/pathVariable/{name}")  // 這裡的路徑應該對應 `my-service` 中的 API
    String getHelloByPathVariable(@PathVariable String name);

    @GetMapping("/hello/requestParam")  // 這裡的路徑應該對應 `my-service` 中的 API
    String getHelloByRequestParam(@RequestParam String name);

    @PostMapping("/hello/createUser")
    String createUser(@RequestBody User user);
}
