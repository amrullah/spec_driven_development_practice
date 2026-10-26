package com.amrullah.cashback_reward_service.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashbackCalculator {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public BigDecimal calculate(BigDecimal purchaseAmount, BigDecimal ratePercent) {
        return purchaseAmount.multiply(ratePercent).divide(ONE_HUNDRED, 2, RoundingMode.DOWN);
    }
}
