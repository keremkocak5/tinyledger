package com.tiny.ledger.controller.v1.dto.outgoing;

import java.util.LinkedList;

public record TransactionBaseResponse(LinkedList<TransactionResponse> transactionResponses) {
}
