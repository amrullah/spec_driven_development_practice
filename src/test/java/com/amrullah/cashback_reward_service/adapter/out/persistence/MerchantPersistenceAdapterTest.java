package com.amrullah.cashback_reward_service.adapter.out.persistence;

import com.amrullah.cashback_reward_service.domain.model.Merchant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MerchantPersistenceAdapter.class)
@DisplayName("MerchantPersistenceAdapter")
class MerchantPersistenceAdapterTest {

    @Autowired
    private MerchantPersistenceAdapter adapter;

    @Autowired
    private TestEntityManager entityManager;

    @ParameterizedTest(name = "rate {0}% survives a save and reload")
    @ValueSource(strings = {"0.01", "1.25", "5", "100"})
    @DisplayName("stores the merchant's rate percent without losing its decimals")
    void roundTripsRatePercent(String ratePercent) {
        adapter.save(new Merchant("merchant-1", "Acme", "Acme Holdings LLC", new BigDecimal(ratePercent)));
        entityManager.flush();
        entityManager.clear();

        Optional<Merchant> found = adapter.findById("merchant-1");

        assertThat(found).hasValueSatisfying(merchant -> {
            assertThat(merchant.displayName()).isEqualTo("Acme");
            assertThat(merchant.legalEntityName()).isEqualTo("Acme Holdings LLC");
            assertThat(merchant.ratePercent()).isEqualByComparingTo(ratePercent);
        });
    }

    @Test
    @DisplayName("returns empty when the merchant does not exist")
    void findsNothingForUnknownMerchant() {
        assertThat(adapter.findById("missing")).isEmpty();
    }
}
