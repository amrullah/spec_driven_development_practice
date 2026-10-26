package com.amrullah.cashback_reward_service.application.port.in;

import java.math.BigDecimal;

public interface RecordPurchaseUseCase {

    BigDecimal record(String purchaseId, String customerId, String merchantId, BigDecimal amount);
}
