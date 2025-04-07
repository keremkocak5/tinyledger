package com.tiny.ledger.service.impl;

import com.tiny.ledger.controller.v1.dto.outgoing.TransactionBaseResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionResponse;
import com.tiny.ledger.enums.TransactionType;
import com.tiny.ledger.exception.TinyLedgerRuntimeException;
import com.tiny.ledger.model.Account;
import com.tiny.ledger.repository.AccountRepository;
import com.tiny.ledger.util.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private AccountRepository accountRepository;

    @Test
    void getTransactionsShouldReturnTransactionsIfAccountFound() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(TestConstants.bigAccount));

        TransactionBaseResponse result = transactionService.getTransactions(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));

        assertThat(result.transactionResponses().size(), is(2));
        assertThat(result.transactionResponses().get(0).id(), is(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
        assertNotNull(result.transactionResponses().get(0).transactionDate());
        assertThat(result.transactionResponses().get(0).transactionType(), is(TransactionType.DEPOSIT));
        assertThat(result.transactionResponses().get(0).currencyCode(), is("GBP"));
        assertThat(result.transactionResponses().get(0).amount(), is(BigDecimal.valueOf(999)));
        assertThat(result.transactionResponses().get(1).id(), is(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7")));
        assertNotNull(result.transactionResponses().get(1).transactionDate());
        assertThat(result.transactionResponses().get(1).transactionType(), is(TransactionType.WITHDRAW));
        assertThat(result.transactionResponses().get(1).currencyCode(), is("GBP"));
        assertThat(result.transactionResponses().get(1).amount(), is(BigDecimal.valueOf(1.11)));
    }

    @Test
    void getTransactionsShouldThrowExceptionIfAccountNotFound() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.empty());

        assertThrows(TinyLedgerRuntimeException.class, () -> transactionService.getTransactions(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }

    @Test
    void createTransactionShouldReturnTransactionResponseWhenBalanceIsPositive() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.valueOf(20.445), "GBP", new LinkedList<>(List.of(TestConstants.transaction1, TestConstants.transaction2)), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        TransactionResponse result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.transactionRequest);

        assertNotNull(result);
        assertThat(result.amount(), is(BigDecimal.valueOf(55.444)));
        assertThat(result.transactionType(), is(TransactionType.DEPOSIT));
        assertThat(result.currencyCode(), is("GBP"));
        assertNotNull(result.id());
        assertNotNull(result.transactionDate());

        assertThat(account.getTransactions().size(), is(3));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(75.889)));
    }

    @Test
    void createTransactionShouldReturnTransactionResponseWhenBalanceIsNegative() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.ONE, "GBP", new LinkedList<>(List.of(TestConstants.transaction1, TestConstants.transaction2)), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        assertThrows(TinyLedgerRuntimeException.class, () -> transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.transactionRequestWithdraw));

        assertThat(account.getTransactions().size(), is(2));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(1)));
    }

    @Test
    void createTransactionShouldReturnTransactionResponseWhenBalanceIsZero() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.valueOf(1.1), "GBP", new LinkedList<>(List.of(TestConstants.transaction1, TestConstants.transaction2)), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        TransactionResponse result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.transactionRequestWithdraw);

        assertThat(account.getTransactions().size(), is(3));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(0.0)));

        assertThat(result.amount(), is(BigDecimal.valueOf(1.1)));
        assertThat(result.transactionType(), is(TransactionType.WITHDRAW));
    }

    @Test
    void createTransactionShouldThrowExceptionResponseWhenAccountNotFound() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.empty());

        assertThrows(TinyLedgerRuntimeException.class, () -> transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.transactionRequestWithdraw));
    }

}