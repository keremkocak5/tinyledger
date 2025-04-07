package com.tiny.ledger.controller.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.ledger.config.GlobalExceptionHandler;
import com.tiny.ledger.controller.v1.dto.incoming.AccountRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.AccountResponse;
import com.tiny.ledger.service.impl.AccountService;
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

import java.util.UUID;

import static com.tiny.ledger.util.TestConstants.OBJECT_MAPPER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;
    private JacksonTester<AccountRequest> packageRequestJacksonTester;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).setControllerAdvice(GlobalExceptionHandler.class).build();
        JacksonTester.initFields(this, OBJECT_MAPPER);
    }

    @Test
    void getBalanceShouldReturnBalanceResponseWhenAccountServiceSuccessful() throws Exception {
        when(accountService.getBalance(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(TestConstants.BALANCE_RESPONSE);

        mockMvc.perform(get("/v1/accounts/id/3fa85f64-5717-4562-b3fc-2c963f66afa6/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"balance\":10}")))
                .andReturn();

        verify(accountService, times(1)).getBalance(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
    }

    @Test
    void getBalanceShouldThrowExceptionWhenAccountServiceNotSuccessful() throws Exception {
        when(accountService.getBalance(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenThrow(new RuntimeException("bad."));

        mockMvc.perform(get("/v1/accounts/id/3fa85f64-5717-4562-b3fc-2c963f66afa6/balance"))
                .andExpect(status().isInternalServerError())
                .andReturn();

        verify(accountService, times(1)).getBalance(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
    }

    @Test
    void getBalanceShouldReturnBalanceResponseWhenInputInvalid() throws Exception {
        mockMvc.perform(get("/v1/accounts/id/hh/balance"))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(accountService, times(0)).getBalance(any());
    }

    @Test
    void createAccountShouldReturnAccountResponseWhenAccountServiceSuccessful() throws Exception {
        when(accountService.createAccount(TestConstants.ACCOUNT_REQUEST)).thenReturn(TestConstants.ACCOUNT_RESPONSE);

        MvcResult result = mockMvc.perform(post("/v1/accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageRequestJacksonTester.write(TestConstants.ACCOUNT_REQUEST).getJson()))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        AccountResponse accountResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(accountResponse, is(TestConstants.ACCOUNT_RESPONSE));
        verify(accountService, times(1)).createAccount(TestConstants.ACCOUNT_REQUEST);
    }

    @Test
    void createAccountShouldReturnExceptionResponseWhenNameTooLong() throws Exception {
        mockMvc.perform(post("/v1/accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageRequestJacksonTester.write(TestConstants.ACCOUNT_REQUEST_LONG_NAME).getJson()))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(accountService, times(0)).createAccount(any());
    }

    @Test
    void createAccountShouldReturnExceptionResponseWhenNameTooShort() throws Exception {
        mockMvc.perform(post("/v1/accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageRequestJacksonTester.write(TestConstants.ACCOUNT_REQUEST_NO_NAME).getJson()))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(accountService, times(0)).createAccount(any());
    }

}