package com.tiny.ledger.service.impl;

import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionBaseResponse;
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
    public TransactionResponse createTransaction(UUID accountId, TransactionRequest transactionRequest) {
        Account existingAccount = findAccountOrElseThrow(accountId);
        Transaction addedTransaction = existingAccount.addTransactionIfBalancePositive(transactionRequest.amount(), transactionRequest.transactionType());
        return getTransactionResponse(addedTransaction);
    }

    @Override
    public TransactionBaseResponse getTransactions(UUID accountId) {
        Account existingAccount = findAccountOrElseThrow(accountId);
        return new TransactionBaseResponse(existingAccount.getTransactions()
                .stream()
                .map(TransactionService::getTransactionResponse)
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    private static TransactionResponse getTransactionResponse(Transaction transaction) {
        return new TransactionResponse(transaction.id(), transaction.amount(), transaction.currencyCode(), transaction.transactionType(), transaction.creationDate());
    }

    private Account findAccountOrElseThrow(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new TinyLedgerRuntimeException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

}
