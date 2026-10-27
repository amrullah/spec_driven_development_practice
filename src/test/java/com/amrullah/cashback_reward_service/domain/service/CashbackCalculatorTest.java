package com.amrullah.cashback_reward_service.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CashbackCalculator")
class CashbackCalculatorTest {

    private final CashbackCalculator calculator = new CashbackCalculator();

    @ParameterizedTest(name = "{0} at {1}% earns {2}")
    @CsvSource({
            "100.00, 5,    5.00",
            "100.00, 1.25, 1.25",
            "20.00,  100,  20.00",
    })
    @DisplayName("calculates purchase amount × rate percent")
    void multipliesAmountByRatePercent(String amount, String ratePercent, String expected) {
        BigDecimal cashback = calculator.calculate(new BigDecimal(amount), new BigDecimal(ratePercent));

        assertThat(cashback).isEqualByComparingTo(expected);
    }

    @ParameterizedTest(name = "{0} at {1}% earns {2}")
    @CsvSource({
            "49.99, 2,    0.99",
            "5.00,  0.01, 0.00",
    })
    @DisplayName("rounds down to the cent, never up")
    void roundsDownToTheCent(String amount, String ratePercent, String expected) {
        BigDecimal cashback = calculator.calculate(new BigDecimal(amount), new BigDecimal(ratePercent));

        assertThat(cashback).isEqualByComparingTo(expected);
    }
}
