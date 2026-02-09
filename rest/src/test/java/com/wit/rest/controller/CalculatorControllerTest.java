package com.wit.rest.controller;

import com.wit.common.api.Operation;
import com.wit.rest.service.CalcGateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalcGateway gateway;

    @Test
    void sum_shouldReturnResult() throws Exception {
        Mockito.when(gateway.calculate(Operation.SUM, new BigDecimal("1"), new BigDecimal("2")))
                .thenReturn(new BigDecimal("3"));

        mockMvc.perform(get("/sum")
                        .param("a", "1")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.result").value(3));
    }

    @Test
    void subtraction_shouldReturnResult() throws Exception {
        Mockito.when(gateway.calculate(Operation.SUBTRACTION, new BigDecimal("5"), new BigDecimal("3")))
                .thenReturn(new BigDecimal("2"));

        mockMvc.perform(get("/subtraction")
                        .param("a", "5")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(2));
    }

    @Test
    void multiplication_shouldReturnResult() throws Exception {
        Mockito.when(gateway.calculate(Operation.MULTIPLICATION, new BigDecimal("4"), new BigDecimal("2")))
                .thenReturn(new BigDecimal("8"));

        mockMvc.perform(get("/multiplication")
                        .param("a", "4")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(8));
    }

    @Test
    void division_shouldReturnResult() throws Exception {
        Mockito.when(gateway.calculate(Operation.DIVISION, new BigDecimal("10"), new BigDecimal("2")))
                .thenReturn(new BigDecimal("5.0000000000"));

        mockMvc.perform(get("/division")
                        .param("a", "10")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(5.0000000000));
    }

    @Test
    void sum_missingParam_shouldReturn400() throws Exception {
        mockMvc.perform(get("/sum")
                        .param("a", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sumWithMissingParameterReturns400() throws Exception {
        mockMvc.perform(get("/sum").param("a", "1"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void sumWithLargeNumbersReturnsCorrectResult() throws Exception {
        BigDecimal a = new BigDecimal("12345678901234567890");
        BigDecimal b = new BigDecimal("98765432109876543210");
        BigDecimal sum = new BigDecimal("111111111011111111100");

        // Stub the mock to return a value
        Mockito.when(gateway.calculate(Operation.SUM, a, b)).thenReturn(sum);

        mockMvc.perform(get("/sum")
                .param("a", a.toString())
                .param("b", b.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(sum.toString()));
    }

    @Test
    void divisionByZeroReturns500() throws Exception {
        BigDecimal a = BigDecimal.TEN;
        BigDecimal b = BigDecimal.ZERO;

        // Stub the mock to throw an exception
        Mockito.when(gateway.calculate(Operation.DIVISION, a, b))
            .thenThrow(new ArithmeticException("Division by zero"));

        mockMvc.perform(get("/division")
                .param("a", a.toString())
                .param("b", b.toString()))
                .andExpect(status().isInternalServerError());
    }


}
