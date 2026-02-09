package com.wit.calculator.kafka;

import com.wit.common.api.CalcRequest;
import com.wit.common.api.CalcResponse;
import com.wit.calculator.service.CalculatorService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class CalcRequestListener {

    private final CalculatorService calculatorService;
    private final KafkaTemplate<String, CalcResponse> kafkaTemplate;
    private final String responseTopic = "calculator-responses"; // or inject via @Value

    public CalcRequestListener(CalculatorService calculatorService,
                               KafkaTemplate<String, CalcResponse> kafkaTemplate) {
        this.calculatorService = calculatorService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
        topics = "calculator-requests",
        groupId = "calculator-service",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(CalcRequest request) {
        BigDecimal result = calculatorService.calculate(
                request.getOperation(),
                request.getA(),
                request.getB()
        );

        CalcResponse response = new CalcResponse(
                request.getRequestId(),
                result,
                "OK"
        );

        kafkaTemplate.send(responseTopic, request.getRequestId(), response);
    }
}
