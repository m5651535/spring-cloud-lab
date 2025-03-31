package com.example.consumer.controller;

import com.example.consumer.service.KafkaProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/send")
    public String send(@RequestParam String message) {
        kafkaProducer.sendMessage(message);
        return "Message sent to Kafka: " + message;
    }
}
