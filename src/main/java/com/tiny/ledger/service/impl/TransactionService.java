package com.tiny.ledger.service.impl;

import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionResponse;
import com.tiny.ledger.enums.ErrorCode;
import com.tiny.ledger.exception.TinyLedgerRuntimeException;
import com.tiny.ledger.model.Account;
import com.tiny.ledger.model.Transaction;
import com.tiny.ledger.repository.AccountRepository;
import com.tiny.ledger.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final AccountRepository accountRepository;

    @Override
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        Account account = getOrElseThrow(transactionRequest.accountId());
        Transaction transaction = account.addTransactionIfBalancePositive(transactionRequest.amount(), transactionRequest.transactionType());
        return getTransactionResponse(transaction);
    }

    @Override
    public LinkedList<TransactionResponse> getTransactions(UUID accountId) {
        Account account = getOrElseThrow(accountId);
        return account.getTransactions()
                .stream()
                .map(trans -> getTransactionResponse(trans))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private static TransactionResponse getTransactionResponse(Transaction transaction) {
        return new TransactionResponse(transaction.id(), transaction.amount(), transaction.currencyCode(), transaction.transactionType(), transaction.transactionDate());
    }

    private Account getOrElseThrow(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new TinyLedgerRuntimeException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

}
