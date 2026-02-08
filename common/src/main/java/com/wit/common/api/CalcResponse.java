package com.wit.common.api;

import java.math.BigDecimal;

public class CalcResponse {
    private final String requestId; // String
    private final BigDecimal result; // BigDecimal, not String
    private final String error;

    public CalcResponse(String requestId, BigDecimal result, String error) {
        this.requestId = requestId;
        this.result = result;
        this.error = error;
    }

    public String requestId() {
        return requestId;
    }

    public BigDecimal result() {
        return result;
    }

    public String error() {
        return error;
    }
}
