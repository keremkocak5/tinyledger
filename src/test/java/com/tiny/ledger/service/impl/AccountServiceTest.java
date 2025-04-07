package com.tiny.ledger.service.impl;

import com.tiny.ledger.controller.v1.dto.outgoing.AccountResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.BalanceResponse;
import com.tiny.ledger.exception.TinyLedgerRuntimeException;
import com.tiny.ledger.model.Account;
import com.tiny.ledger.repository.AccountRepository;
import com.tiny.ledger.util.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private Clock clock;
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    void getBalanceShouldReturnBalanceWhenAccountFound() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(TestConstants.NEW_ACCOUNT));

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
        when(accountRepository.saveOrUpdate(any())).thenReturn(TestConstants.NEW_ACCOUNT);
        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        when(clock.instant()).thenReturn(Instant.ofEpochMilli(1744058699198L));

        AccountResponse result = accountService.createAccount(TestConstants.ACCOUNT_REQUEST);

        verify(accountRepository).saveOrUpdate(accountArgumentCaptor.capture());
        assertThat(accountArgumentCaptor.getValue().getAccountOwnerName(), is("...................................................................................................."));
        assertThat(accountArgumentCaptor.getValue().getId(), is(result.id()));
        assertThat(accountArgumentCaptor.getValue().getCreationDate().toInstant().toString(), is("2025-04-07T20:44:59.198Z"));
        assertThat(accountArgumentCaptor.getValue().getCurrencyCode(), is("GBP"));
        assertThat(accountArgumentCaptor.getValue().getBalance(), is(BigDecimal.ZERO));
        assertThat(accountArgumentCaptor.getValue().getTransactions().size(), is(0));

        assertThat(result.accountOwnerName(), is("...................................................................................................."));
        assertThat(result.balance(), is(BigDecimal.ZERO));
        assertNotNull(result.id());
    }

    @Test
    void createAccountShouldThrowExceptionWhenTheUserIsVeryUnluckyOrLucky() {
        when(clock.instant()).thenReturn(Instant.now());
        when(accountRepository.findById(any())).thenReturn(Optional.of(TestConstants.NEW_ACCOUNT));

        assertThrows(TinyLedgerRuntimeException.class, () -> accountService.createAccount(TestConstants.ACCOUNT_REQUEST));
    }

}