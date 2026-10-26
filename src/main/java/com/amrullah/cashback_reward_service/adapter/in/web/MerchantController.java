package com.amrullah.cashback_reward_service.adapter.in.web;

import com.amrullah.cashback_reward_service.application.port.in.RegisterMerchantUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
class MerchantController {

    record RegisterMerchantRequest(String displayName, String legalEntityName, BigDecimal ratePercent) {
    }

    record RegisterMerchantResponse(String merchantId) {
    }

    private final RegisterMerchantUseCase registerMerchant;

    MerchantController(RegisterMerchantUseCase registerMerchant) {
        this.registerMerchant = registerMerchant;
    }

    @PostMapping("/merchants")
    @ResponseStatus(HttpStatus.CREATED)
    RegisterMerchantResponse register(@Valid @RequestBody RegisterMerchantRequest request) {
        return new RegisterMerchantResponse(
                registerMerchant.register(request.displayName(), request.legalEntityName(), request.ratePercent()));
    }
}
