package com.example.producer.controller;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/produce")
public class ProducerController {
    private final StreamBridge streamBridge;

    public ProducerController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping("/sendJson")
    public String sendJson(@RequestParam String message) {
        Map<String, String> jsonPayload = new HashMap<>();
        jsonPayload.put("message", message);
        jsonPayload.put("timestamp", String.valueOf(System.currentTimeMillis()));

        streamBridge.send("jsonOutput", MessageBuilder.withPayload(jsonPayload)
                .setHeader("contentType", "application/json")
                .build());
        return "JSON message sent: " + message;
    }

    @GetMapping("/sendString")
    public String sendString(@RequestParam String message) {
        streamBridge.send("stringOutput", MessageBuilder.withPayload(message)
                .setHeader("contentType", "text/plain")
                .build());
        return "String message sent: " + message;
    }

    @GetMapping("/sendByte")
    public String sendByte(@RequestParam String message) {
        byte[] bytePayload = message.getBytes(StandardCharsets.UTF_8);
        streamBridge.send("byteOutput", MessageBuilder.withPayload(bytePayload)
                .setHeader("contentType", "application/octet-stream")
                .build());
        return "Byte message sent: " + message;
    }
}
