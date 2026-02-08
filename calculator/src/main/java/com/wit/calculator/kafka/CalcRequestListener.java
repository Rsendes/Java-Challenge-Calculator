package com.wit.calculator.kafka;

import com.wit.common.api.CalcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CalcRequestListener {

    private static final Logger log = LoggerFactory.getLogger(CalcRequestListener.class);

    @KafkaListener(topics = "${app.kafka.topic.requests}")
    public void listen(CalcRequest request) {
        log.info("Received CalcRequest: {}", request);
    }
}
