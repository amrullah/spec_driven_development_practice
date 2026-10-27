package com.amrullah.cashback_reward_service.adapter.in.web;

import com.amrullah.cashback_reward_service.application.port.in.RecordPurchaseUseCase;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
@DisplayName("PurchaseController")
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecordPurchaseUseCase recordPurchase;

    @Test
    @DisplayName("POST /purchases responds 201 with the purchase id and the cashback earned")
    void recordsPurchaseAndReturnsCashbackEarned() throws Exception {
        given(recordPurchase.record("purchase-1", "customer-1", "merchant-1", new BigDecimal("49.99")))
                .willReturn(new BigDecimal("0.99"));

        String response = mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"purchaseId": "purchase-1", "customerId": "customer-1", "merchantId": "merchant-1", "amount": 49.99}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.purchaseId").value("purchase-1"))
                .andReturn().getResponse().getContentAsString();

        Object cashbackEarned = JsonPath.read(response, "$.cashbackEarned");
        assertThat(new BigDecimal(String.valueOf(cashbackEarned))).isEqualByComparingTo("0.99");
    }
}
