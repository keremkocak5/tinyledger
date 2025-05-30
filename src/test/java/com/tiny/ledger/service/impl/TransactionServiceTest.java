package com.tiny.ledger.service.impl;

import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
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
import java.math.RoundingMode;
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
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(TestConstants.BIG_ACCOUNT));

        TransactionBaseResponse result = transactionService.getTransactions(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));

        assertThat(result.transactions().size(), is(2));
        assertThat(result.transactions().get(0).id(), is(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
        assertNotNull(result.transactions().get(0).transactionDate());
        assertThat(result.transactions().get(0).transactionType(), is(TransactionType.DEPOSIT));
        assertThat(result.transactions().get(0).currencyCode(), is("GBP"));
        assertThat(result.transactions().get(0).amount(), is(BigDecimal.valueOf(999)));
        assertThat(result.transactions().get(1).id(), is(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7")));
        assertNotNull(result.transactions().get(1).transactionDate());
        assertThat(result.transactions().get(1).transactionType(), is(TransactionType.WITHDRAW));
        assertThat(result.transactions().get(1).currencyCode(), is("GBP"));
        assertThat(result.transactions().get(1).amount(), is(BigDecimal.valueOf(1.11)));
    }

    @Test
    void getTransactionsShouldThrowExceptionIfAccountNotFound() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.empty());

        assertThrows(TinyLedgerRuntimeException.class, () -> transactionService.getTransactions(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }

    @Test
    void createTransactionShouldReturnTransactionResponseWhenBalanceLeadsPositiveRoundingDown() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.valueOf(20.445), "GBP", new LinkedList<>(List.of(TestConstants.TRANSACTION_1, TestConstants.TRANSACTION_2)), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        TransactionResponse result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.TRANSACTION_REQUEST_DEPOSIT_THIRD_DECIMAL_4);

        assertNotNull(result);
        assertThat(result.amount(), is(BigDecimal.valueOf(55.44)));
        assertThat(result.transactionType(), is(TransactionType.DEPOSIT));
        assertThat(result.currencyCode(), is("GBP"));
        assertNotNull(result.id());
        assertNotNull(result.transactionDate());

        assertThat(account.getTransactions().size(), is(3));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(75.885)));
    }

    @Test
    void createTransactionShouldReturnTransactionResponseWhenBalanceLeadsPositiveRoundingUp() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.valueOf(20.445), "GBP", new LinkedList<>(List.of(TestConstants.TRANSACTION_1, TestConstants.TRANSACTION_2)), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        TransactionResponse result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.TRANSACTION_REQUEST_DEPOSIT_THIRD_DECIMAL_9);

        assertNotNull(result);
        assertThat(result.amount(), is(BigDecimal.valueOf(55.45)));
        assertThat(result.transactionType(), is(TransactionType.DEPOSIT));
        assertThat(result.currencyCode(), is("GBP"));
        assertNotNull(result.id());
        assertNotNull(result.transactionDate());

        assertThat(account.getTransactions().size(), is(3));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(75.895)));
    }

    @Test
    void createTransactionShouldThrowExceptionWhenBalanceLeadsNegative() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.ONE, "GBP", new LinkedList<>(List.of(TestConstants.TRANSACTION_1, TestConstants.TRANSACTION_2)), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        assertThrows(TinyLedgerRuntimeException.class, () -> transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.TRANSACTION_REQUEST_WITHDRAW));

        assertThat(account.getTransactions().size(), is(2));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(1)));
    }

    @Test
    void createTransactionShouldReturnTransactionResponseWhenBalanceLeadsZero() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.valueOf(1.1), "GBP", new LinkedList<>(List.of(TestConstants.TRANSACTION_1, TestConstants.TRANSACTION_2)), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        TransactionResponse result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.TRANSACTION_REQUEST_WITHDRAW);

        assertThat(account.getTransactions().size(), is(3));
        assertThat(account.getBalance(), is(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)));

        assertThat(result.amount(), is(BigDecimal.valueOf(1.1).setScale(2, RoundingMode.HALF_UP)));
        assertThat(result.transactionType(), is(TransactionType.WITHDRAW));
    }

    @Test
    void createTransactionShouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.empty());

        assertThrows(TinyLedgerRuntimeException.class, () -> transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), TestConstants.TRANSACTION_REQUEST_WITHDRAW));
    }

    @Test
    void createTransactionShouldRoundUpCorrectlyForMultipleDeposit() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.ZERO, "GBP", new LinkedList<>(), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        TransactionResponse result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.10), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(0.10).setScale(2, RoundingMode.HALF_UP)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(0.10).setScale(2, RoundingMode.HALF_UP)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.100), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(0.10).setScale(2, RoundingMode.HALF_UP)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(0.20).setScale(2, RoundingMode.HALF_UP)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.105), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(0.11)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(0.31)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.109), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(0.11)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(0.42)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.99), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(0.99)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(1.41)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.5), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(0.50).setScale(2, RoundingMode.HALF_UP)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(1.91)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.9989), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(1.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(2.91)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.999), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(1.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(3.91)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(1.000), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(1.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(4.91)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(1.001), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(1.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(5.91)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(1.009), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(1.01)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(6.92)));

        result = transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(1.00999), TransactionType.DEPOSIT));
        assertThat(result.amount(), is(BigDecimal.valueOf(1.01)));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(7.93)));
    }

    @Test
    void createTransactionShouldThrowExceptionWhenAmountLeadsZero() {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.ONE, "GBP", new LinkedList<>(List.of(TestConstants.TRANSACTION_1, TestConstants.TRANSACTION_2)), Date.from(Instant.now()));
        when(accountRepository.findById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))).thenReturn(Optional.of(account));

        assertThrows(TinyLedgerRuntimeException.class, () -> transactionService.createTransaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), new TransactionRequest(BigDecimal.valueOf(0.00001), TransactionType.DEPOSIT)));

        assertThat(account.getTransactions().size(), is(2));
        assertThat(account.getBalance(), is(BigDecimal.valueOf(1)));
    }

}