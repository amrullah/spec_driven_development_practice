package com.amrullah.cashback_reward_service.adapter.in.web;

import com.amrullah.cashback_reward_service.application.port.in.RegisterMerchantUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MerchantController.class)
@DisplayName("MerchantController")
class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterMerchantUseCase registerMerchant;

    @Test
    @DisplayName("POST /merchants responds 201 with the id of the registered merchant")
    void registersMerchantAndReturnsItsId() throws Exception {
        given(registerMerchant.register("Acme", "Acme Holdings LLC", new BigDecimal("1.25")))
                .willReturn("merchant-1");

        mockMvc.perform(post("/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"displayName": "Acme", "legalEntityName": "Acme Holdings LLC", "ratePercent": 1.25}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.merchantId").value("merchant-1"));
    }
}
