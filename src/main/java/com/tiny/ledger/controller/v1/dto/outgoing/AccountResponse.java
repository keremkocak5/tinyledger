package com.tiny.ledger.controller.v1.dto.outgoing;

import java.util.UUID;

public record AccountResponse(
        UUID id,
        String accountOwnerName) {
}
