package com.amrullah.cashback_reward_service.adapter.out.persistence;

import com.amrullah.cashback_reward_service.domain.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CustomerPersistenceAdapter.class)
@DisplayName("CustomerPersistenceAdapter")
class CustomerPersistenceAdapterTest {

    @Autowired
    private CustomerPersistenceAdapter adapter;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("stores a customer and finds it again by id")
    void roundTripsCustomer() {
        adapter.save(new Customer("customer-1", "Jane", "Doe", "+14155550101"));
        entityManager.flush();
        entityManager.clear();

        assertThat(adapter.findById("customer-1"))
                .contains(new Customer("customer-1", "Jane", "Doe", "+14155550101"));
    }

    @Test
    @DisplayName("returns empty when the customer does not exist")
    void findsNothingForUnknownCustomer() {
        assertThat(adapter.findById("missing")).isEmpty();
    }
}
