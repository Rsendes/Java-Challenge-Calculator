package com.wit.rest.kafka;

import com.wit.common.api.CalcResponse;
import com.wit.rest.service.CalcGateway;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CalcResponseListener {

    private final CalcGateway gateway;

    public CalcResponseListener(CalcGateway gateway) {
        this.gateway = gateway;
    }

    @KafkaListener(topics = "${app.kafka.topic.responses}")
    public void onMessage(CalcResponse response) {
        gateway.complete(response);
    }
}
