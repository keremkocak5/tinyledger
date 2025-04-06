package com.tiny.ledger.service;

import com.tiny.ledger.controller.v1.dto.incoming.AccountRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.BalanceResponse;
import com.tiny.ledger.model.Account;

import java.util.UUID;

public interface IAccountService {

    BalanceResponse getBalance(UUID accountId);

    Account createAccount(AccountRequest accountRequest);

}
