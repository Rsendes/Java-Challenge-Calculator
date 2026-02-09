package com.wit.common.api;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CalcRequest {

    private String requestId; // remove final
    private Operation operation;
    private BigDecimal a;
    private BigDecimal b;

    // No-arg constructor for Jackson
    public CalcRequest() {}

    @JsonCreator
    public CalcRequest(
            @JsonProperty("requestId") String requestId,
            @JsonProperty("operation") Operation operation,
            @JsonProperty("a") BigDecimal a,
            @JsonProperty("b") BigDecimal b
    ) {
        this.requestId = requestId;
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    public String getRequestId() { return requestId; }
    public Operation getOperation() { return operation; }
    public BigDecimal getA() { return a; }
    public BigDecimal getB() { return b; }

    // Optional setters if you want Jackson to set via no-arg constructor
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setOperation(Operation operation) { this.operation = operation; }
    public void setA(BigDecimal a) { this.a = a; }
    public void setB(BigDecimal b) { this.b = b; }
}
