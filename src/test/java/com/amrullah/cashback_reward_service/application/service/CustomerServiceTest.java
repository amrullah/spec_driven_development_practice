package com.amrullah.cashback_reward_service.application.service;

import com.amrullah.cashback_reward_service.application.port.out.CustomerRepository;
import com.amrullah.cashback_reward_service.domain.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CustomerService")
class CustomerServiceTest {

    @Test
    @DisplayName("saves the customer under a newly generated id and returns that id")
    void savesCustomerUnderGeneratedId() {
        RecordingCustomers customers = new RecordingCustomers();
        CustomerService service = new CustomerService(customers);

        String id = service.register("Jane", "Doe", "+14155550101");

        assertThat(id).isNotBlank();
        assertThat(customers.saved).containsExactly(new Customer(id, "Jane", "Doe", "+14155550101"));
    }

    @Test
    @DisplayName("generates a different id for each registration")
    void generatesDistinctIds() {
        CustomerService service = new CustomerService(new RecordingCustomers());

        String first = service.register("Jane", "Doe", "+14155550101");
        String second = service.register("Jane", "Doe", "+14155550102");

        assertThat(first).isNotEqualTo(second);
    }

    private static class RecordingCustomers implements CustomerRepository {

        private final List<Customer> saved = new ArrayList<>();

        @Override
        public void save(Customer customer) {
            saved.add(customer);
        }

        @Override
        public Optional<Customer> findById(String id) {
            throw new UnsupportedOperationException();
        }
    }
}
