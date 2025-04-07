package com.tiny.ledger.util;

import com.tiny.ledger.controller.v1.dto.incoming.AccountRequest;
import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.AccountResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.BalanceResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionResponse;
import com.tiny.ledger.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class TestConstants {

    private TestConstants() {
    }

    public static final AccountRequest accountRequestNoName = new AccountRequest("");
    public static final AccountRequest accountRequestLongName = new AccountRequest(".".repeat(101));
    public static final AccountRequest accountRequest = new AccountRequest(".".repeat(100));
    public static final AccountResponse accountResponse = new AccountResponse(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak");
    public static final BalanceResponse balanceResponse = new BalanceResponse(BigDecimal.TEN);
    public static final TransactionRequest transactionRequest = new TransactionRequest(BigDecimal.valueOf(55.444), TransactionType.DEPOSIT);
    public static final TransactionRequest transactionRequestNegative = new TransactionRequest(BigDecimal.valueOf(-55.444), TransactionType.DEPOSIT);
    public static final TransactionResponse transactionResponse1 = new TransactionResponse(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), BigDecimal.TEN, "GBP", TransactionType.DEPOSIT, Date.from(Instant.now()));
    public static final TransactionResponse transactionResponse2 = new TransactionResponse(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"), BigDecimal.ONE, "GBP", TransactionType.WITHDRAW, Date.from(Instant.now()));
}
