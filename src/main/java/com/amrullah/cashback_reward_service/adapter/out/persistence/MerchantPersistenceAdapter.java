package com.amrullah.cashback_reward_service.adapter.out.persistence;

import com.amrullah.cashback_reward_service.application.port.out.MerchantRepository;
import com.amrullah.cashback_reward_service.domain.model.Merchant;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class MerchantPersistenceAdapter implements MerchantRepository {

    private final MerchantJpaRepository jpaRepository;

    MerchantPersistenceAdapter(MerchantJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Merchant merchant) {
        jpaRepository.save(new MerchantEntity(
                merchant.id(), merchant.displayName(), merchant.legalEntityName(), merchant.ratePercent()));
    }

    @Override
    public Optional<Merchant> findById(String id) {
        return jpaRepository.findById(id).map(entity -> new Merchant(
                entity.getId(), entity.getDisplayName(), entity.getLegalEntityName(), entity.getRatePercent()));
    }
}
