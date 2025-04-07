package com.tiny.ledger.controller.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.ledger.config.GlobalExceptionHandler;
import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionResponse;
import com.tiny.ledger.service.impl.TransactionService;
import com.tiny.ledger.util.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;
    private JacksonTester<TransactionRequest> packageRequestJacksonTester;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).setControllerAdvice(GlobalExceptionHandler.class).build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void getBalanceShouldReturnBalanceResponseWhenAccountServiceSuccessful() throws Exception {
        when(transactionService.getTransactions(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(new LinkedList<>(List.of(TestConstants.transactionResponse1, TestConstants.transactionResponse2)));

        MvcResult result = mockMvc.perform(get("/v1/transactions/accountid/3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        LinkedList<TransactionResponse> transactionResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<LinkedList<TransactionResponse>>() {
        });
        assertThat(transactionResponse.size(), is(2));
        assertThat(transactionResponse.get(0), is(TestConstants.transactionResponse1));
        assertThat(transactionResponse.get(1), is(TestConstants.transactionResponse2));
        verify(transactionService, times(1)).getTransactions(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
    }

    @Test
    void getBalanceShouldThrowExceptionWhenInputInvalid() throws Exception {
        mockMvc.perform(get("/v1/transactions/accountid/xxx"))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(transactionService, times(0)).getTransactions(any());
    }

    @Test
    void createTransactionShouldReturnTransactionResponseWhenTransactionServiceSucceeds() throws Exception {
        when(transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.transactionRequest)).thenReturn(TestConstants.transactionResponse1);

        MvcResult result = mockMvc.perform(post("/v1/transactions/accountid/3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageRequestJacksonTester.write(TestConstants.transactionRequest).getJson()))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        TransactionResponse transactionResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<TransactionResponse>() {
        });
        assertThat(transactionResponse, is(TestConstants.transactionResponse1));
        verify(transactionService, times(1)).createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.transactionRequest);
    }

    @Test
    void createTransactionShouldThrowExceptionWhenAmountNegative() throws Exception {
        mockMvc.perform(post("/v1/transactions/accountid/3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageRequestJacksonTester.write(TestConstants.transactionRequestNegative).getJson()))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(transactionService, times(0)).createTransaction(any(), any());
    }

    @Test
    void createTransactionShouldThrowExceptionWhenAccountNumberInvalid() throws Exception {
        mockMvc.perform(post("/v1/transactions/accountid/3fa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageRequestJacksonTester.write(TestConstants.transactionRequestNegative).getJson()))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(transactionService, times(0)).createTransaction(any(), any());
    }
}