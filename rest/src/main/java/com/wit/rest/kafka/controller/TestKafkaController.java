package com.wit.rest.controller;

import com.wit.rest.kafka.TestProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestKafkaController {

    private final TestProducer testProducer;

    public TestKafkaController(TestProducer testProducer) {
        this.testProducer = testProducer;
    }

    @GetMapping("/test-message")
    public String sendTestMessage(@RequestParam(defaultValue = "1") String key,
                                  @RequestParam(defaultValue = "Hello Kafka") String message) {
        testProducer.sendTestMessage(key, message);
        return "Message sent: key=" + key + ", value=" + message;
    }
}
