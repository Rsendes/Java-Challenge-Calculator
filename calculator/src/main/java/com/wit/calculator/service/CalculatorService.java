package com.wit.calculator.service;

import com.wit.common.api.Operation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    public BigDecimal calculate(Operation op, BigDecimal a, BigDecimal b) {
        return switch (op) {
            case SUM -> a.add(b);
            case SUBTRACTION -> a.subtract(b);
            case MULTIPLICATION -> a.multiply(b);
            case DIVISION -> a.divide(b, 10, RoundingMode.HALF_UP);
        };
    }
}
