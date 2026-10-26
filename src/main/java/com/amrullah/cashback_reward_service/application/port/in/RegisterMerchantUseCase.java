package com.amrullah.cashback_reward_service.application.port.in;

import java.math.BigDecimal;

public interface RegisterMerchantUseCase {

    String register(String displayName, String legalEntityName, BigDecimal ratePercent);
}
