package com.wit.rest.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestProducer {

    private static final Logger log = LoggerFactory.getLogger(TestProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String requestTopic;

    public TestProducer(KafkaTemplate<String, String> kafkaTemplate,
                        @Value("${app.kafka.topic.requests}") String requestTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.requestTopic = requestTopic;
    }

    public void sendTestMessage(String key, String message) {
        log.info("Sending test message key={} value={}", key, message);
        kafkaTemplate.send(requestTopic, key, message);
    }
}
