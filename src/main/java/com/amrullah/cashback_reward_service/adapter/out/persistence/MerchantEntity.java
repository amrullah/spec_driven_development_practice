package com.amrullah.cashback_reward_service.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "merchant")
class MerchantEntity {

    @Id
    private String id;
    private String displayName;
    private String legalEntityName;
    @Column(precision = 5, scale = 2)
    private BigDecimal ratePercent;

    protected MerchantEntity() {
    }

    MerchantEntity(String id, String displayName, String legalEntityName, BigDecimal ratePercent) {
        this.id = id;
        this.displayName = displayName;
        this.legalEntityName = legalEntityName;
        this.ratePercent = ratePercent;
    }

    String getId() {
        return id;
    }

    String getDisplayName() {
        return displayName;
    }

    String getLegalEntityName() {
        return legalEntityName;
    }

    BigDecimal getRatePercent() {
        return ratePercent;
    }
}
