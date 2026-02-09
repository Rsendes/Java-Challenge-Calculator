package com.wit.rest.kafka;

import com.wit.common.api.CalcResponse;
import com.wit.rest.service.CalcGateway;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CalcResponseListener {

    private final CalcGateway calcGateway;

    public CalcResponseListener(CalcGateway calcGateway) {
        this.calcGateway = calcGateway;
    }

    @KafkaListener(
            topics = "${app.kafka.topic.responses}",
            groupId = "rest-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(CalcResponse response) {
        calcGateway.complete(response); // completes the pending request
    }
}
