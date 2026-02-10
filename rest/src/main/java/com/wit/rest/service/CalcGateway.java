package com.wit.rest.service;

import com.wit.common.api.CalcRequest;
import com.wit.common.api.CalcResponse;
import com.wit.common.api.Operation;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CalcGateway {

    private final KafkaTemplate<String, CalcRequest> kafkaTemplate;
    private final String requestTopic;
    private final long timeoutMs;

    // Map of requestId -> CompletableFuture for async responses
    private final ConcurrentHashMap<String, CompletableFuture<CalcResponse>> pending =
            new ConcurrentHashMap<>();

    public CalcGateway(
            KafkaTemplate<String, CalcRequest> kafkaTemplate,
            @Value("${app.kafka.topic.requests}") String requestTopic,
            @Value("${app.kafka.reply-timeout-ms}") long timeoutMs
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.requestTopic = requestTopic;
        this.timeoutMs = timeoutMs;
    }

    /**
     * Sends a calculation request and waits for the response asynchronously.
     * Propagates requestId from HTTP MDC if present.
     */
    public BigDecimal calculate(Operation operation, BigDecimal a, BigDecimal b) {
        // Reuse requestId from MDC if present, else generate a new one
        String requestId = MDC.get("requestId");
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        CalcRequest request = new CalcRequest(requestId, operation, a, b);

        CompletableFuture<CalcResponse> future = new CompletableFuture<>();
        pending.put(requestId, future);

        // Send the request to Kafka
        kafkaTemplate.send(requestTopic, requestId, request);

        try {
            CalcResponse response = future.get(timeoutMs, TimeUnit.MILLISECONDS);

            if (response.getError() != null) {
                throw new IllegalArgumentException(response.getError());
            }

            return response.getResult();

        } catch (Exception e) {
            throw new RuntimeException("Calculation failed: " + e.getMessage(), e);

        } finally {
            pending.remove(requestId);
        }
    }

    /**
     * Kafka listener for calculator responses
     */
    @KafkaListener(
            topics = "${app.kafka.topic.responses}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "calcResponseKafkaListenerContainerFactory"
    )
    public void listenResponses(CalcResponse response) {
        complete(response);
    }

    /**
     * Completes a pending calculation when a response is received.
     */
    public void complete(CalcResponse response) {
        CompletableFuture<CalcResponse> future = pending.get(response.getRequestId());
        if (future != null) {
            future.complete(response);
        }
    }
}
