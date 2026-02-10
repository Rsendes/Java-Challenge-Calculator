package com.wit.calculator.kafka;

import com.wit.calculator.service.CalculatorService;
import com.wit.common.api.CalcRequest;
import com.wit.common.api.CalcResponse;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CalcRequestListener {

    private static final Logger log = LoggerFactory.getLogger(CalcRequestListener.class);
    public static final String MDC_KEY = "requestId";

    private final CalculatorService calculatorService;
    private final KafkaTemplate<String, CalcResponse> kafkaTemplate;
    private final String responseTopic;

    public CalcRequestListener(
            CalculatorService calculatorService,
            KafkaTemplate<String, CalcResponse> kafkaTemplate,
            @Value("${app.kafka.topic.responses}") String responseTopic
    ) {
        this.calculatorService = calculatorService;
        this.kafkaTemplate = kafkaTemplate;
        this.responseTopic = responseTopic;
    }

    @KafkaListener(
            topics = "${app.kafka.topic.requests}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(CalcRequest request) {

        MDC.put(MDC_KEY, request.getRequestId());
        try {
            log.info("Received calculation request: {} {} {}", request.getOperation(), request.getA(), request.getB());

            BigDecimal result = calculatorService.calculate(
                    request.getOperation(),
                    request.getA(),
                    request.getB()
            );

            log.info("Calculation result: {}", result);

            CalcResponse response = new CalcResponse(
                    request.getRequestId(),
                    result,
                    null
            );

            kafkaTemplate.send(responseTopic, request.getRequestId(), response);
            log.info("Response sent for requestId={}", request.getRequestId());

        } finally {
            MDC.remove(MDC_KEY);
        }
    }
}
