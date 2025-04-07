package com.tiny.ledger.controller.v1.dto.outgoing;


import org.springframework.lang.NonNull;

import java.util.LinkedList;

public record TransactionBaseResponse(@NonNull LinkedList<TransactionResponse> transactions) {
}
