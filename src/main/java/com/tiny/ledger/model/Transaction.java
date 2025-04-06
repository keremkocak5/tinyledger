package com.tiny.ledger.model;

import com.tiny.ledger.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record Transaction(@NonNull @NotEmpty UUID id,
                          @NonNull @NotEmpty @DecimalMin(value = "0.0") BigDecimal amount,
                          @NonNull @NotEmpty @Size(min = 1, max = 4) String currencyCode,
                          @NonNull @NotEmpty TransactionType transactionType,
                          @NonNull @NotEmpty Date transactionDate) {
}
