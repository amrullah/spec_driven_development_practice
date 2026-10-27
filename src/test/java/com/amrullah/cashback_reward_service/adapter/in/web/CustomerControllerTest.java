package com.amrullah.cashback_reward_service.adapter.in.web;

import com.amrullah.cashback_reward_service.application.port.in.RegisterCustomerUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@DisplayName("CustomerController")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterCustomerUseCase registerCustomer;

    @Test
    @DisplayName("POST /customers responds 201 with the id of the registered customer")
    void registersCustomerAndReturnsItsId() throws Exception {
        given(registerCustomer.register("Jane", "Doe", "+14155550101")).willReturn("customer-1");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "Jane", "lastName": "Doe", "mobileNumber": "+14155550101"}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("customer-1"));
    }
}
