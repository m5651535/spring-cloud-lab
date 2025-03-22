package com.example.consumer.fallback;

import com.example.consumer.client.MyServiceClient;
import dto.User;
import org.springframework.stereotype.Component;

@Component
public class MyServiceFallBack implements MyServiceClient {
    @Override
    public String getHelloByPathVariable(String name) {
        return "my-service 不可用，請稍後再試！";
    }

    @Override
    public String getHelloByRequestParam(String name) {
        return "my-service 不可用，請稍後再試！";
    }

    @Override
    public String createUser(User user) {
        return "my-service 不可用，請稍後再試！";
    }
}
