package com.amrullah.cashback_reward_service.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface CustomerJpaRepository extends JpaRepository<CustomerEntity, String> {
}
