package com.wit.rest.controller;

import com.wit.common.api.Operation;
import com.wit.rest.service.CalcGateway;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    private final CalcGateway gateway;

    public CalculatorController(CalcGateway gateway) {
        this.gateway = gateway;
    }

    @GetMapping("/sum")
    public ResponseEntity<Map<String, BigDecimal>> sum(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        return ResponseEntity.ok(Map.of("result", gateway.calculate(Operation.SUM, a, b)));
    }

    @GetMapping("/subtraction")
    public ResponseEntity<Map<String, BigDecimal>> subtraction(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        return ResponseEntity.ok(Map.of("result", gateway.calculate(Operation.SUBTRACTION, a, b)));
    }

    @GetMapping("/multiplication")
    public ResponseEntity<Map<String, BigDecimal>> multiplication(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        return ResponseEntity.ok(Map.of("result", gateway.calculate(Operation.MULTIPLICATION, a, b)));
    }

    @GetMapping("/division")
    public ResponseEntity<Map<String, BigDecimal>> division(
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        return ResponseEntity.ok(Map.of("result", gateway.calculate(Operation.DIVISION, a, b)));
    }
}
