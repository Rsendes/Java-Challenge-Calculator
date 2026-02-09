package com.wit.rest.service;

import com.wit.common.api.CalcRequest;
import com.wit.common.api.CalcResponse;
import com.wit.common.api.Operation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalcGatewayTest {

    @Test
    void calculate_shouldSendRequestAndReturnResponse() throws Exception {
        KafkaTemplate<String, CalcRequest> kafkaTemplate = Mockito.mock(KafkaTemplate.class);

        CalcGateway gateway = new CalcGateway(
                kafkaTemplate,
                "calculator-requests",
                2000
        );

        // run calculate() in another thread because it blocks waiting for response
        var thread = new Thread(() -> {
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
        KafkaTemplate<String, CalcRequest> kafkaTemplate = Mockito.mock(KafkaTemplate.class);

        CalcGateway gateway = new CalcGateway(
                kafkaTemplate,
                "calculator-requests",
                200
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gateway.calculate(Operation.SUM, new BigDecimal("1"), new BigDecimal("2"))
        );

        assertTrue(ex.getMessage().contains("Calculation failed"));
    }
}
