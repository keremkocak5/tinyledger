package com.tiny.ledger.service;

import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionResponse;

import java.util.LinkedList;
import java.util.UUID;

public interface ITransactionService {

    TransactionResponse createTransaction(UUID accountId, TransactionRequest transactionRequest);

    LinkedList<TransactionResponse> getTransactions(UUID accountId);

}
