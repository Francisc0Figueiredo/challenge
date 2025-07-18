package com.calculator.rest;

import java.math.BigDecimal;
import java.util.UUID;

public class CalculatorRequest {

    private UUID requestId;
    private String operation;
    private BigDecimal A;
    private BigDecimal B;

    public CalculatorRequest() {
    }

    public CalculatorRequest(UUID requestId, String operation, BigDecimal A, BigDecimal B) {
        this.requestId = requestId;
        this.operation = operation;
        this.A = A;
        this.B = B;
    }

    // Getters & Setters
    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public BigDecimal getA() {
        return A;
    }

    public void setA(BigDecimal A) {
        this.A = A;
    }

    public BigDecimal getB() {
        return B;
    }

    public void setB(BigDecimal B) {
        this.B = B;
    }
}
