package com.tiny.ledger.service.impl;

import com.tiny.ledger.controller.v1.dto.incoming.AccountRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.AccountResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.BalanceResponse;
import com.tiny.ledger.enums.ErrorCode;
import com.tiny.ledger.exception.TinyLedgerRuntimeException;
import com.tiny.ledger.model.Account;
import com.tiny.ledger.repository.AccountRepository;
import com.tiny.ledger.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import static com.tiny.ledger.util.Constants.GBP;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    @Override
    public BalanceResponse getBalance(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new TinyLedgerRuntimeException(ErrorCode.ACCOUNT_NOT_FOUND));
        return new BalanceResponse(account.getBalance());
    }

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account newAccount = new Account(UUID.randomUUID(), accountRequest.accountOwnerName(), BigDecimal.ZERO, GBP, new LinkedList<>(), Date.from(Instant.now()));
        accountRepository.findById(newAccount.getId()).ifPresent(s -> {
            throw new TinyLedgerRuntimeException(ErrorCode.DUPLICATE_UUID);
        });
        accountRepository.saveOrUpdate(newAccount);
        return new AccountResponse(newAccount.getId(), newAccount.getAccountOwnerName(), BigDecimal.ZERO);
    }
}
