package com.wit.calculator.service;

import com.wit.common.api.Operation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private final CalculatorService service = new CalculatorService();

    @Test
    void sum_shouldReturnCorrectResult() {
        BigDecimal result = service.calculate(Operation.SUM, new BigDecimal("1"), new BigDecimal("2"));
        assertEquals(new BigDecimal("3"), result);
    }

    @Test
    void subtraction_shouldReturnCorrectResult() {
        BigDecimal result = service.calculate(Operation.SUBTRACTION, new BigDecimal("5"), new BigDecimal("3"));
        assertEquals(new BigDecimal("2"), result);
    }

    @Test
    void multiplication_shouldReturnCorrectResult() {
        BigDecimal result = service.calculate(Operation.MULTIPLICATION, new BigDecimal("4"), new BigDecimal("2"));
        assertEquals(new BigDecimal("8"), result);
    }

    @Test
    void division_shouldReturnCorrectResultWithScale10() {
        BigDecimal result = service.calculate(Operation.DIVISION, new BigDecimal("10"), new BigDecimal("2"));
        assertEquals(new BigDecimal("5.0000000000"), result);
    }

    @Test
    void division_shouldRoundHalfUpToScale10() {
        BigDecimal result = service.calculate(Operation.DIVISION, new BigDecimal("1"), new BigDecimal("3"));
        assertEquals(new BigDecimal("0.3333333333"), result);
    }

    @Test
    void shouldSupportArbitraryPrecisionDecimals() {
        BigDecimal result = service.calculate(Operation.SUM, new BigDecimal("0.1"), new BigDecimal("0.2"));
        assertEquals(new BigDecimal("0.3"), result);
    }

    @Test
    void divisionByZero_shouldThrowException() {
        assertThrows(ArithmeticException.class, () ->
                service.calculate(Operation.DIVISION, new BigDecimal("10"), BigDecimal.ZERO)
        );
    }

    @Test
    void divisionByZeroThrows() {
        BigDecimal a = BigDecimal.valueOf(10);
        BigDecimal b = BigDecimal.ZERO;
        assertThrows(ArithmeticException.class, () -> service.calculate(Operation.DIVISION, a, b));
    }

    @Test
    void largeNumbersCalculateCorrectly() {
        BigDecimal a = new BigDecimal("12345678901234567890");
        BigDecimal b = new BigDecimal("98765432109876543210");
        BigDecimal result = service.calculate(Operation.SUM, a, b);
        assertEquals(new BigDecimal("111111111011111111100"), result);
    }

    @Test
    void negativeNumbersWork() {
        BigDecimal a = BigDecimal.valueOf(-5);
        BigDecimal b = BigDecimal.valueOf(-3);
        assertEquals(BigDecimal.valueOf(-8), service.calculate(Operation.SUM, a, b));
    }

    @Test
    void decimalDivisionPrecision() {
        BigDecimal a = new BigDecimal("1");
        BigDecimal b = new BigDecimal("3");
        BigDecimal result = service.calculate(Operation.DIVISION, a, b);
        assertEquals(new BigDecimal("0.3333333333"), result); // matches 10 decimal places
    }

}
