package com.tiny.ledger.controller.v1;

import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionResponse;
import com.tiny.ledger.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.UUID;

@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping(value = "/accountid/{accountId}")
    @Operation(summary = "Create a transaction")
    public TransactionResponse createTransaction(@PathVariable UUID accountId, @RequestBody @Valid TransactionRequest transactionRequest) {
        return transactionService.createTransaction(accountId, transactionRequest);
    }

    @GetMapping(value = "/accountid/{accountId}")
    @Operation(summary = "Get transactions of an account")
    public LinkedList<TransactionResponse> getTransactions(@PathVariable UUID accountId) {
        return transactionService.getTransactions(accountId);
    }

}
