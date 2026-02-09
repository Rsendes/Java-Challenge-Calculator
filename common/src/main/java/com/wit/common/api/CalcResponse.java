package com.wit.common.api;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CalcResponse {

    private String requestId;
    private BigDecimal result;
    private String error;

    public CalcResponse() {}

    @JsonCreator
    public CalcResponse(
            @JsonProperty("requestId") String requestId,
            @JsonProperty("result") BigDecimal result,
            @JsonProperty("error") String error
    ) {
        this.requestId = requestId;
        this.result = result;
        this.error = error;
    }

    public String getRequestId() { return requestId; }
    public BigDecimal getResult() { return result; }
    public String getError() { return error; }

    // Optional setters
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setResult(BigDecimal result) { this.result = result; }
    public void setError(String error) { this.error = error; }
}
