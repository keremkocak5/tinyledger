package com.tiny.ledger.util;

import com.tiny.ledger.controller.v1.dto.incoming.AccountRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.AccountResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.BalanceResponse;

import java.math.BigDecimal;
import java.util.UUID;

public class TestConstants {

    private TestConstants() {}

    public static final AccountRequest accountRequestNoName = new AccountRequest("");
    public static final AccountRequest accountRequestLongName = new AccountRequest(".".repeat(101));
    public static final AccountRequest accountRequest = new AccountRequest(".".repeat(100));
    public static final AccountResponse accountResponse = new AccountResponse(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak");
    public static final BalanceResponse balanceResponse = new BalanceResponse(BigDecimal.TEN);
}
