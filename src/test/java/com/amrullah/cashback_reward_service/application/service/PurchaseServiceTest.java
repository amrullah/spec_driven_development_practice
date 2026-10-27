package com.amrullah.cashback_reward_service.application.service;

import com.amrullah.cashback_reward_service.application.port.out.MerchantRepository;
import com.amrullah.cashback_reward_service.domain.model.Merchant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PurchaseService")
class PurchaseServiceTest {

    @ParameterizedTest(name = "{0} at the merchant's {1}% rate earns {2}")
    @CsvSource({
            "100.00, 5,    5.00",
            "100.00, 2,    2.00",
            "49.99,  2,    0.99",
    })
    @DisplayName("records the cashback calculated from the merchant's configured rate")
    void recordsCashbackAtTheMerchantsRate(String amount, String ratePercent, String expected) {
        MerchantRepository merchants = new StubMerchants(
                new Merchant("merchant-1", "Acme", "Acme Holdings LLC", new BigDecimal(ratePercent)));
        PurchaseService service = new PurchaseService(merchants);

        BigDecimal cashback = service.record("purchase-1", "customer-1", "merchant-1", new BigDecimal(amount));

        assertThat(cashback).isEqualByComparingTo(expected);
    }

    private record StubMerchants(Merchant merchant) implements MerchantRepository {

        @Override
        public void save(Merchant merchant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Merchant> findById(String id) {
            return Optional.of(merchant).filter(m -> m.id().equals(id));
        }
    }
}
