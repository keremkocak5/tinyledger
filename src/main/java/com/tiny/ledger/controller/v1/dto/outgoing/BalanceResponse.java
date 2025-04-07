package com.tiny.ledger.controller.v1.dto.outgoing;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public record BalanceResponse(@NonNull BigDecimal balance) {
}
