package com.tiny.ledger.controller.v1;

import com.tiny.ledger.controller.v1.dto.incoming.TransactionRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionBaseResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.TransactionResponse;
import com.tiny.ledger.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping(value = "/account/{accountId}")
    @Operation(summary = "Create a transaction")
    public TransactionResponse createTransaction(@PathVariable @NonNull UUID accountId,
                                                 @RequestBody @NonNull @Valid TransactionRequest transactionRequest) {
        return transactionService.createTransaction(accountId, transactionRequest);
    }

    @GetMapping(value = "/account/{accountId}")
    @Operation(summary = "Get transactions of an account")
    public TransactionBaseResponse getTransactions(@PathVariable @NonNull UUID accountId) {
        return transactionService.getTransactions(accountId);
    }

}
