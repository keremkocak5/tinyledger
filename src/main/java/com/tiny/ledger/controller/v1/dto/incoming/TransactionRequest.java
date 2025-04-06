package com.tiny.ledger.controller.v1.dto.incoming;

import com.tiny.ledger.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequest(
        UUID accountId,
        @NonNull @DecimalMin(value = "0.0") BigDecimal amount,
        @NonNull TransactionType transactionType) {
}
