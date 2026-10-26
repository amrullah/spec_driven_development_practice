package com.amrullah.cashback_reward_service.application.service;

import com.amrullah.cashback_reward_service.application.port.in.RegisterCustomerUseCase;
import com.amrullah.cashback_reward_service.application.port.out.CustomerRepository;
import com.amrullah.cashback_reward_service.domain.model.Customer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class CustomerService implements RegisterCustomerUseCase {

    private final CustomerRepository customers;

    CustomerService(CustomerRepository customers) {
        this.customers = customers;
    }

    @Override
    public String register(String firstName, String lastName, String mobileNumber) {
        String id = UUID.randomUUID().toString();
        customers.save(new Customer(id, firstName, lastName, mobileNumber));
        return id;
    }
}
