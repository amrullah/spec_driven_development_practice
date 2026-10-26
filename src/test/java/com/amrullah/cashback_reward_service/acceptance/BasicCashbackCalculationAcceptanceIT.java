package com.amrullah.cashback_reward_service.acceptance;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Basic Cashback Calculation")
class BasicCashbackCalculationAcceptanceIT {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Must calculate cashback as purchase amount × the merchant's rate, rounded down to the cent")
    class CashbackIsAmountTimesRateRoundedDown {

        @Test
        @DisplayName("The one where $100.00 at a 5% merchant earns $5.00")
        void normalCase() throws Exception {
            assertCashback("+14155550101", 1, "5", "100.00", "5.00");
        }

        @Test
        @DisplayName("The one where $100.00 at a 1.25% merchant earns $1.25")
        void fractionalRate() throws Exception {
            assertCashback("+14155550102", 2, "1.25", "100.00", "1.25");
        }

        @Test
        @DisplayName("The one where $49.99 at a 2% merchant earns $0.99, because 0.9998 rounds down and never up")
        void roundsDownNeverUp() throws Exception {
            assertCashback("+14155550103", 3, "2", "49.99", "0.99");
        }

        @Test
        @DisplayName("The one where $20.00 at a 100% merchant earns $20.00, the highest allowed rate")
        void highestAllowedRate() throws Exception {
            assertCashback("+14155550104", 4, "100", "20.00", "20.00");
        }

        @Test
        @DisplayName("The one where a $5.00 purchase at a 0.01% merchant calculates $0.0005. It qualifies, but it rounds down to $0.00 and is recorded as earning nothing")
        void qualifiesButRoundsDownToNothing() throws Exception {
            assertCashback("+14155550105", 5, "0.01", "5.00", "0.00");
        }
    }

    private void assertCashback(String customerMobileNumber, int merchantNumber, String ratePercent, String amount, String expectedCashback) throws Exception {
        String purchaseId = "purchase-" + UUID.randomUUID();

        String customerResponse = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "Jane", "lastName": "Doe", "mobileNumber": "%s"}"""
                                .formatted(customerMobileNumber)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String customerId = JsonPath.read(customerResponse, "$.customerId");

        String merchantResponse = mockMvc.perform(post("/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"displayName": "Acme %d", "legalEntityName": "Acme Holdings %d LLC", "ratePercent": %s}"""
                                .formatted(merchantNumber, merchantNumber, ratePercent)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String merchantId = JsonPath.read(merchantResponse, "$.merchantId");

        String response = mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"purchaseId": "%s", "customerId": "%s", "merchantId": "%s", "amount": %s}"""
                                .formatted(purchaseId, customerId, merchantId, amount)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Object cashbackEarned = JsonPath.read(response, "$.cashbackEarned");
        assertThat(new BigDecimal(String.valueOf(cashbackEarned))).isEqualByComparingTo(expectedCashback);
    }
}
