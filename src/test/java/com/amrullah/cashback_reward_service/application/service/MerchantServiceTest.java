package com.amrullah.cashback_reward_service.application.service;

import com.amrullah.cashback_reward_service.application.port.out.MerchantRepository;
import com.amrullah.cashback_reward_service.domain.model.Merchant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MerchantService")
class MerchantServiceTest {

    @Test
    @DisplayName("saves the merchant with its configured rate under a newly generated id and returns that id")
    void savesMerchantUnderGeneratedId() {
        RecordingMerchants merchants = new RecordingMerchants();
        MerchantService service = new MerchantService(merchants);

        String id = service.register("Acme", "Acme Holdings LLC", new BigDecimal("1.25"));

        assertThat(id).isNotBlank();
        assertThat(merchants.saved).containsExactly(
                new Merchant(id, "Acme", "Acme Holdings LLC", new BigDecimal("1.25")));
    }

    @Test
    @DisplayName("generates a different id for each registration")
    void generatesDistinctIds() {
        MerchantService service = new MerchantService(new RecordingMerchants());

        String first = service.register("Acme", "Acme Holdings LLC", new BigDecimal("5"));
        String second = service.register("Acme", "Acme Holdings LLC", new BigDecimal("5"));

        assertThat(first).isNotEqualTo(second);
    }

    private static class RecordingMerchants implements MerchantRepository {

        private final List<Merchant> saved = new ArrayList<>();

        @Override
        public void save(Merchant merchant) {
            saved.add(merchant);
        }

        @Override
        public Optional<Merchant> findById(String id) {
            throw new UnsupportedOperationException();
        }
    }
}
