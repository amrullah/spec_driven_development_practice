package com.amrullah.cashback_reward_service.application.port.out;

import com.amrullah.cashback_reward_service.domain.model.Merchant;

import java.util.Optional;

public interface MerchantRepository {

    void save(Merchant merchant);

    Optional<Merchant> findById(String id);
}
