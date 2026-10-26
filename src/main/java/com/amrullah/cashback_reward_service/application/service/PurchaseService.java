package com.amrullah.cashback_reward_service.application.service;

import com.amrullah.cashback_reward_service.application.port.in.RecordPurchaseUseCase;
import com.amrullah.cashback_reward_service.application.port.out.MerchantRepository;
import com.amrullah.cashback_reward_service.domain.service.CashbackCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
class PurchaseService implements RecordPurchaseUseCase {

    private final MerchantRepository merchants;
    private final CashbackCalculator calculator = new CashbackCalculator();

    PurchaseService(MerchantRepository merchants) {
        this.merchants = merchants;
    }

    @Override
    public BigDecimal record(String purchaseId, String customerId, String merchantId, BigDecimal amount) {
        BigDecimal ratePercent = merchants.findById(merchantId).orElseThrow().ratePercent();
        return calculator.calculate(amount, ratePercent);
    }
}
