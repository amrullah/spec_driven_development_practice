package com.amrullah.cashback_reward_service.domain.model;

import java.math.BigDecimal;

public record Merchant(String id, String displayName, String legalEntityName, BigDecimal ratePercent) {
}
