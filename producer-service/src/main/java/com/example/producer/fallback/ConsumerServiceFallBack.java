package com.example.producer.fallback;

import com.example.producer.client.ConsumerServiceClient;
import dto.User;
import org.springframework.stereotype.Component;

@Component
public class ConsumerServiceFallBack implements ConsumerServiceClient {
    @Override
    public String getHelloByPathVariable(String name) {
        return "consumer-service 不可用，請稍後再試！";
    }

    @Override
    public String getHelloByRequestParam(String name) {
        return "consumer-service 不可用，請稍後再試！";
    }

    @Override
    public String createUser(User user) {
        return "consumer-service 不可用，請稍後再試！";
    }
}
