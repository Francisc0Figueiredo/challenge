package com.calculator.core;

import java.math.BigDecimal;
import java.util.UUID;

public class KafkaMessage {

    private String operation;
    private BigDecimal A;
    private BigDecimal B;
    private BigDecimal result;

    public KafkaMessage() {

    }

    public KafkaMessage(UUID requestId, String operation, BigDecimal A, BigDecimal B) {
        this.operation = operation;
        this.A = A;
        this.B = B;
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

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }
}