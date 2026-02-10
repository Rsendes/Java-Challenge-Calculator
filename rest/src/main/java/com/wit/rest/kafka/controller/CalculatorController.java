package com.wit.rest.controller;

import com.wit.rest.service.CalcGateway;
import com.wit.common.api.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
public class CalculatorController {

    private static final Logger log = LoggerFactory.getLogger(CalculatorController.class);
    private final CalcGateway calcGateway;

    public CalculatorController(CalcGateway calcGateway) {
        this.calcGateway = calcGateway;
    }

    @GetMapping("/sum")
    public Map<String, BigDecimal> sum(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        log.info("Received sum request: a={}, b={}", a, b);
        BigDecimal result = calcGateway.calculate(Operation.SUM, a, b);
        log.info("Sum result: {}", result);
        return Map.of("result", result);                                // satisfies JSON test
    }

    @GetMapping("/subtraction")
    public Map<String, BigDecimal> subtraction(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        log.info("Received subtraction request: a={}, b={}", a, b);
        BigDecimal result = calcGateway.calculate(Operation.SUBTRACTION, a, b);
        log.info("Subtraction result: {}", result);
        return Map.of("result", result);  // wrap in Map so JSON has "result"
    }

    @GetMapping("/multiplication")
    public Map<String, BigDecimal> multiplication(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        log.info("Received multiplication request: a={}, b={}", a, b);
        BigDecimal result = calcGateway.calculate(Operation.MULTIPLICATION, a, b);
        log.info("Multiplication result: {}", result);
        return Map.of("result", result);
    }

    @GetMapping("/division")
    public Map<String, BigDecimal> division(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        log.info("Received division request: a={}, b={}", a, b);

        try {
            BigDecimal result = calcGateway.calculate(Operation.DIVISION, a, b);
            log.info("Division result: {}", result);
            return Map.of("result", result);
        } catch (ArithmeticException e) {
            log.error("Division by zero error: a={}, b={}", a, b);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Division by zero", e);
        }
    }

}
