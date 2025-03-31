package com.example.consumer.service;

import constants.KafkaConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        try {
            kafkaTemplate.send(KafkaConstants.TOPIC_NAME, message).get(); // 同步發送
            System.out.println("Message sent to " + KafkaConstants.TOPIC_NAME + ": " + message);
        } catch (Exception e) {
            System.err.println("Failed to send: " + e.getMessage());
        }
    }
}
