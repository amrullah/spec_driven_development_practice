package com.amrullah.cashback_reward_service.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
class CustomerEntity {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String mobileNumber;

    protected CustomerEntity() {
    }

    CustomerEntity(String id, String firstName, String lastName, String mobileNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
    }

    String getId() {
        return id;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    String getMobileNumber() {
        return mobileNumber;
    }
}
