package com.amrullah.cashback_reward_service.application.port.in;

public interface RegisterCustomerUseCase {

    String register(String firstName, String lastName, String mobileNumber);
}
