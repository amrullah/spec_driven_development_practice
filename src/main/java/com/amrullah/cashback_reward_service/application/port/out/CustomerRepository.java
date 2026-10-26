package com.amrullah.cashback_reward_service.application.port.out;

import com.amrullah.cashback_reward_service.domain.model.Customer;

import java.util.Optional;

public interface CustomerRepository {

    void save(Customer customer);

    Optional<Customer> findById(String id);
}
