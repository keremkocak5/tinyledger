package com.tiny.ledger.model;

import com.tiny.ledger.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record Transaction(@NonNull @NotEmpty UUID id,
                          @NonNull @NotEmpty UUID accountId,
                          @NonNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal amount,
                          @NonNull @Size(min = 1, max = 4) String currencyCode,
                          @NonNull @NotEmpty TransactionType transactionType,
                          @NonNull @NotEmpty Date creationDate) {
}
