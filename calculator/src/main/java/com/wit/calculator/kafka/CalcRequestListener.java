package com.wit.calculator.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CalcRequestListener {

    private static final Logger log = LoggerFactory.getLogger(CalcRequestListener.class);

    @KafkaListener(topics = "${app.kafka.topic.requests}")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received message: key={} value={}", record.key(), record.value());
        // For now, just log — no calculation yet
    }
}
