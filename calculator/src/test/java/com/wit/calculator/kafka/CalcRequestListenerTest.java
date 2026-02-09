package com.wit.calculator.kafka;

import com.wit.calculator.service.CalculatorService;
import com.wit.common.api.CalcRequest;
import com.wit.common.api.CalcResponse;
import com.wit.common.api.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalcRequestListenerTest {

    private CalculatorService calculatorService;
    private KafkaTemplate<String, CalcResponse> kafkaTemplate;
    private CalcRequestListener listener;

    @BeforeEach
    void setUp() {
        calculatorService = mock(CalculatorService.class);
        kafkaTemplate = mock(KafkaTemplate.class);

        listener = new CalcRequestListener(calculatorService, kafkaTemplate);
    }

    @Test
    void listen_shouldCalculateAndSendResponse() {
        // given
        String requestId = "req-123";
        CalcRequest request = new CalcRequest(
                requestId,
                Operation.SUM,
                new BigDecimal("10"),
                new BigDecimal("5")
        );

        BigDecimal expected = new BigDecimal("15");
        when(calculatorService.calculate(Operation.SUM, new BigDecimal("10"), new BigDecimal("5")))
                .thenReturn(expected);

        // when
        listener.listen(request);

        // then - calculator called
        verify(calculatorService, times(1))
                .calculate(Operation.SUM, new BigDecimal("10"), new BigDecimal("5"));

        // then - kafka send called
        ArgumentCaptor<CalcResponse> responseCaptor = ArgumentCaptor.forClass(CalcResponse.class);

        verify(kafkaTemplate, times(1))
                .send(eq("calculator-responses"), eq(requestId), responseCaptor.capture());

        CalcResponse sentResponse = responseCaptor.getValue();

        assertEquals(requestId, sentResponse.getRequestId());
        assertEquals(expected, sentResponse.getResult());
        assertNull(sentResponse.getError());
    }
}
