package com.example.consumerService.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class MessageConsumer {
    @Bean
    public Consumer<Map<String, String>> jsonInput() {
        return payload -> System.out.println("Received JSON: " + payload);
    }

    @Bean
    public Consumer<String> stringInput() {
        return payload -> System.out.println("Received String: " + payload);
    }

    @Bean
    public Consumer<byte[]> byteInput() {
        return payload -> System.out.println("Received Byte: " + new String(payload, StandardCharsets.UTF_8));
    }
}
