package com.example.consumer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "my-service")
public interface MyServiceClient {
    @GetMapping("/hello")  // 這裡的路徑應該對應 `my-service` 中的 API
    String getHello();
}
