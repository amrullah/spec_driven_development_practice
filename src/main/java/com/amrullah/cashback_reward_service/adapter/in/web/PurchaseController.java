package com.amrullah.cashback_reward_service.adapter.in.web;

import com.amrullah.cashback_reward_service.application.port.in.RecordPurchaseUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
class PurchaseController {

    record RecordPurchaseRequest(String purchaseId, String customerId, String merchantId, BigDecimal amount) {
    }

    record RecordPurchaseResponse(String purchaseId, BigDecimal cashbackEarned) {
    }

    private final RecordPurchaseUseCase recordPurchase;

    PurchaseController(RecordPurchaseUseCase recordPurchase) {
        this.recordPurchase = recordPurchase;
    }

    @PostMapping("/purchases")
    @ResponseStatus(HttpStatus.CREATED)
    RecordPurchaseResponse record(@Valid @RequestBody RecordPurchaseRequest request) {
        BigDecimal cashbackEarned = recordPurchase.record(
                request.purchaseId(), request.customerId(), request.merchantId(), request.amount());
        return new RecordPurchaseResponse(request.purchaseId(), cashbackEarned);
    }
}
