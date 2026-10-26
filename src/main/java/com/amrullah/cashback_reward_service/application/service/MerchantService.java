package com.amrullah.cashback_reward_service.application.service;

import com.amrullah.cashback_reward_service.application.port.in.RegisterMerchantUseCase;
import com.amrullah.cashback_reward_service.application.port.out.MerchantRepository;
import com.amrullah.cashback_reward_service.domain.model.Merchant;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
class MerchantService implements RegisterMerchantUseCase {

    private final MerchantRepository merchants;

    MerchantService(MerchantRepository merchants) {
        this.merchants = merchants;
    }

    @Override
    public String register(String displayName, String legalEntityName, BigDecimal ratePercent) {
        String id = UUID.randomUUID().toString();
        merchants.save(new Merchant(id, displayName, legalEntityName, ratePercent));
        return id;
    }
}
