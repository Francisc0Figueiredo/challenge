package com.calculator.core;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    public BigDecimal sum(BigDecimal A, BigDecimal B) {
        return A.add(B);
    }

    public BigDecimal subtract(BigDecimal A, BigDecimal B) {
        return A.subtract(B);
    }

    public BigDecimal multiply(BigDecimal A, BigDecimal B) {
        return A.multiply(B);
    }

    public BigDecimal divide(BigDecimal A, BigDecimal B) {
        if (B.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return A.divide(B, 10, RoundingMode.HALF_UP).stripTrailingZeros(); 
    }
}
