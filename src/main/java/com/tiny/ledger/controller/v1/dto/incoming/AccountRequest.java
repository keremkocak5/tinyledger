package com.tiny.ledger.controller.v1.dto.incoming;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record AccountRequest(
        @NonNull @NotEmpty @Valid @Size(min = 1, max = 100) String accountOwnerName) {
}
