package com.wit.rest.service;

import com.wit.common.api.CalcRequest;
import com.wit.common.api.CalcResponse;
import com.wit.common.api.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class CalcGatewayTest {

    private CalcGateway gateway;
    private KafkaTemplate<String, CalcRequest> kafkaTemplate;

    @BeforeEach
    void setUp() {
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        gateway = new CalcGateway(
                kafkaTemplate,
                "calculator-requests",
                2000
        );
    }

    @Test
    void calculate_shouldSendRequestAndReturnResponse() throws Exception {
        // run calculate() in another thread because it blocks waiting for response
        Thread thread = new Thread(() -> {
            BigDecimal result = gateway.calculate(Operation.SUM, new BigDecimal("1"), new BigDecimal("2"));
            assertEquals(new BigDecimal("3"), result);
        });

        thread.start();

        // Capture requestId from the send() call
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<CalcRequest> requestCaptor = ArgumentCaptor.forClass(CalcRequest.class);

        // wait until kafkaTemplate.send() gets called
        Thread.sleep(100);

        Mockito.verify(kafkaTemplate).send(Mockito.eq("calculator-requests"), keyCaptor.capture(), requestCaptor.capture());

        String requestId = keyCaptor.getValue();
        assertNotNull(requestId);

        // Simulate calculator service response arriving via Kafka
        CalcResponse response = new CalcResponse(requestId, new BigDecimal("3"), null);
        gateway.listenResponses(response);

        thread.join();
    }

    @Test
    void calculate_shouldTimeoutIfNoResponse() {
        // Override gateway with very short timeout
        CalcGateway shortTimeoutGateway = new CalcGateway(kafkaTemplate, "calculator-requests", 200);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                shortTimeoutGateway.calculate(Operation.SUM, new BigDecimal("1"), new BigDecimal("2"))
        );

        assertTrue(ex.getMessage().contains("Calculation failed"));
    }

    @Test
    void completeResolvesPendingFuture() throws Exception {
        // Start a calculation in a separate thread
        Thread thread = new Thread(() -> {
            BigDecimal result = gateway.calculate(Operation.SUM, BigDecimal.TEN, BigDecimal.ZERO);
            assertEquals(BigDecimal.TEN, result);
        });

        thread.start();

        // Simulate the response arriving
        CalcResponse response = new CalcResponse("some-request-id", BigDecimal.TEN, null);
        gateway.listenResponses(response);

        thread.join();
    }


    @Test
    void calculateThrowsOnTimeout() {
        // Use a short timeout gateway to force timeout
        CalcGateway shortTimeoutGateway = new CalcGateway(kafkaTemplate, "calculator-requests", 50);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                shortTimeoutGateway.calculate(Operation.SUM, BigDecimal.ONE, BigDecimal.TEN)
        );

        assertTrue(ex.getMessage().contains("Calculation failed"));
    }
}
