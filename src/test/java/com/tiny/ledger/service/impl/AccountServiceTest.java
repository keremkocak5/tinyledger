package com.tiny.ledger.service.impl;

import com.tiny.ledger.controller.v1.dto.outgoing.AccountResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.BalanceResponse;
import com.tiny.ledger.exception.TinyLedgerRuntimeException;
import com.tiny.ledger.repository.AccountRepository;
import com.tiny.ledger.util.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;

    @Test
    void getBalanceShouldReturnBalanceWhenAccountFound() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(TestConstants.newAccount));

        BalanceResponse result = accountService.getBalance(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));

        assertThat(result.balance(), is(BigDecimal.TEN));
    }

    @Test
    void getBalanceShouldThrowExceptionWhenRepositoryFails() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenThrow(new RuntimeException("bad"));

        assertThrows(RuntimeException.class, () -> accountService.getBalance(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }

    @Test
    void getBalanceShouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.empty());

        assertThrows(TinyLedgerRuntimeException.class, () -> accountService.getBalance(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }

    @Test
    void createAccountShouldReturnAccountWhenRepositoryPersists() {
        when(accountRepository.saveOrUpdate(any())).thenReturn(TestConstants.newAccount);

        AccountResponse result = accountService.createAccount(TestConstants.accountRequest);
        // kerem buraya captor ekle
        assertThat(result.accountOwnerName(), is("...................................................................................................."));
        assertThat(result.balance(), is(BigDecimal.ZERO));
        assertNotNull(result.id());
    }

}