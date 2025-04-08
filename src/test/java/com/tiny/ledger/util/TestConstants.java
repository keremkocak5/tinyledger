package com.tiny.ledger.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.ledger.controller.v1.dto.incoming.AccountRequest;
import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.AccountResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.BalanceResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionResponse;
import com.tiny.ledger.enums.TransactionType;
import com.tiny.ledger.model.Account;
import com.tiny.ledger.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TestConstants {

    private TestConstants() {
    }

    public static final AccountRequest ACCOUNT_REQUEST_NO_NAME = new AccountRequest("");
    public static final AccountRequest ACCOUNT_REQUEST_LONG_NAME = new AccountRequest(".".repeat(101));
    public static final AccountRequest ACCOUNT_REQUEST = new AccountRequest(".".repeat(100));
    public static final AccountResponse ACCOUNT_RESPONSE = new AccountResponse(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.ZERO);
    public static final BalanceResponse BALANCE_RESPONSE = new BalanceResponse(BigDecimal.TEN);
    public static final TransactionRequest TRANSACTION_REQUEST_DEPOSIT_THIRD_DECIMAL_4 = new TransactionRequest(BigDecimal.valueOf(55.444), TransactionType.DEPOSIT);
    public static final TransactionRequest TRANSACTION_REQUEST_DEPOSIT_THIRD_DECIMAL_9 = new TransactionRequest(BigDecimal.valueOf(55.449), TransactionType.DEPOSIT);
    public static final TransactionRequest TRANSACTION_REQUEST_WITHDRAW = new TransactionRequest(BigDecimal.valueOf(1.1), TransactionType.WITHDRAW);
    public static final TransactionRequest TRANSACTION_REQUEST_NEGATIVE = new TransactionRequest(BigDecimal.valueOf(-55.444), TransactionType.DEPOSIT);
    public static final TransactionRequest TRANSACTION_REQUEST_ZERO = new TransactionRequest(BigDecimal.valueOf(0.0), TransactionType.DEPOSIT);
    public static final TransactionResponse TRANSACTION_RESPONSE_1 = new TransactionResponse(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), BigDecimal.TEN, "GBP", TransactionType.DEPOSIT, Date.from(Instant.now()));
    public static final TransactionResponse TRANSACTION_RESPONSE_2 = new TransactionResponse(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"), BigDecimal.ONE, "GBP", TransactionType.WITHDRAW, Date.from(Instant.now()));
    public static final Transaction TRANSACTION_1 = new Transaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), BigDecimal.valueOf(999), "GBP", TransactionType.DEPOSIT, Date.from(Instant.now()));
    public static final Transaction TRANSACTION_2 = new Transaction(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"), UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), BigDecimal.valueOf(1.11), "GBP", TransactionType.WITHDRAW, Date.from(Instant.now()));
    public static final Account NEW_ACCOUNT = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.TEN, "GBP", new LinkedList<>(), Date.from(Instant.now()));
    public static final Account BIG_ACCOUNT = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.valueOf(456789.9993), "GBP", new LinkedList<>(List.of(TRANSACTION_1, TRANSACTION_2)), Date.from(Instant.now()));

    public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
