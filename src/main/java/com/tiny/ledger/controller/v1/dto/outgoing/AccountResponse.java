package com.tiny.ledger.controller.v1.dto.outgoing;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        @NonNull UUID id,
        @NonNull @NotEmpty String accountOwnerName,
        @NonNull BigDecimal balance) {
}
