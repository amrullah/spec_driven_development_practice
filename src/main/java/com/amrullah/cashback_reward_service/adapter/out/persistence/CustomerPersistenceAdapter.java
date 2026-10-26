package com.amrullah.cashback_reward_service.adapter.out.persistence;

import com.amrullah.cashback_reward_service.application.port.out.CustomerRepository;
import com.amrullah.cashback_reward_service.domain.model.Customer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class CustomerPersistenceAdapter implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    CustomerPersistenceAdapter(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Customer customer) {
        jpaRepository.save(new CustomerEntity(
                customer.id(), customer.firstName(), customer.lastName(), customer.mobileNumber()));
    }

    @Override
    public Optional<Customer> findById(String id) {
        return jpaRepository.findById(id).map(entity -> new Customer(
                entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getMobileNumber()));
    }
}
