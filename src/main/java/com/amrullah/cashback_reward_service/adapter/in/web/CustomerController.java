package com.amrullah.cashback_reward_service.adapter.in.web;

import com.amrullah.cashback_reward_service.application.port.in.RegisterCustomerUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class CustomerController {

    record RegisterCustomerRequest(String firstName, String lastName, String mobileNumber) {
    }

    record RegisterCustomerResponse(String customerId) {
    }

    private final RegisterCustomerUseCase registerCustomer;

    CustomerController(RegisterCustomerUseCase registerCustomer) {
        this.registerCustomer = registerCustomer;
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    RegisterCustomerResponse register(@Valid @RequestBody RegisterCustomerRequest request) {
        return new RegisterCustomerResponse(
                registerCustomer.register(request.firstName(), request.lastName(), request.mobileNumber()));
    }
}
