package com.wit.rest.service;

import com.wit.common.api.CalcRequest;
import com.wit.common.api.CalcResponse;
import com.wit.common.api.Operation;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
     *
     * @param operation The calculation operation (SUM, SUBTRACT, MULTIPLY, DIVIDE)
     * @param a Operand A
     * @param b Operand B
     * @return The calculation result
     */
    public BigDecimal calculate(Operation operation, BigDecimal a, BigDecimal b) {
        // Generate requestId as String
        String requestId = UUID.randomUUID().toString();

        CalcRequest request = new CalcRequest(requestId, operation, a, b);

        CompletableFuture<CalcResponse> future = new CompletableFuture<>();
        pending.put(requestId, future);

        // Send the request to Kafka
        kafkaTemplate.send(requestTopic, requestId, request);

        try {
            CalcResponse response = future.get(timeoutMs, TimeUnit.MILLISECONDS);

            if (response.error() != null) {
                throw new IllegalArgumentException(response.error());
            }

            // Return the BigDecimal result directly
            return response.result();

        } catch (Exception e) {
            throw new RuntimeException("Calculation failed: " + e.getMessage(), e);

        } finally {
            pending.remove(requestId);
        }
    }

    /**
     * Completes a pending calculation when a response is received.
     *
     * @param response The calculation response
     */
    public void complete(CalcResponse response) {
        // Lookup using String requestId
        CompletableFuture<CalcResponse> future = pending.get(response.requestId());
        if (future != null) {
            future.complete(response);
        }
    }
}
