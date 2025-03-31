package com.example.myService.service;

import constants.KafkaConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @KafkaListener(topics = KafkaConstants.TOPIC_NAME, groupId = "my-group")
    public void listen(String message) {
        System.out.println("Received from Kafka: " + message);
        // 在這裡處理業務邏輯
    }
}
