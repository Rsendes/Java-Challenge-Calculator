package com.wit.common.api;

import java.math.BigDecimal;

public class CalcRequest {
    private final String requestId; // String, not UUID
    private final Operation operation;
    private final BigDecimal a;
    private final BigDecimal b;

    public CalcRequest(String requestId, Operation operation, BigDecimal a, BigDecimal b) {
        this.requestId = requestId;
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    public String getRequestId() {
        return requestId;
    }

    public Operation getOperation() {
        return operation;
    }

    public BigDecimal getA() {
        return a;
    }

    public BigDecimal getB() {
        return b;
    }
}
