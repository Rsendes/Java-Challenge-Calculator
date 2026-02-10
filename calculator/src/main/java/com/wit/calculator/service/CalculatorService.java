package com.wit.calculator.service;

import com.wit.common.api.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    private static final Logger log = LoggerFactory.getLogger(CalculatorService.class);
    public static final String MDC_KEY = "requestId";

    public BigDecimal calculate(Operation operation, BigDecimal a, BigDecimal b) {
        // Grab requestId from MDC if present
        String requestId = MDC.get(MDC_KEY);

        log.info("[{}] Calculating {} {} {}", requestId, a, operation, b);

        BigDecimal result;

        switch (operation) {
            case SUM -> result = a.add(b);
            case SUBTRACTION -> result = a.subtract(b);
            case MULTIPLICATION -> result = a.multiply(b);
            case DIVISION -> {
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    log.error("[{}] Division by zero: {} / {}", requestId, a, b);
                    throw new ArithmeticException("Division by zero");
                }
                // Use scale 10 and RoundingMode.HALF_UP for precision
                result = a.divide(b, 10, RoundingMode.HALF_UP);
            }
            default -> throw new IllegalArgumentException("Unknown operation " + operation);
        }

        log.info("[{}] Result for {} {} {} = {}", requestId, a, operation, b, result);
        return result;
    }
}
