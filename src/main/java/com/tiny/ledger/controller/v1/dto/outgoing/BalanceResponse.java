package com.tiny.ledger.controller.v1.dto.outgoing;

import java.math.BigDecimal;

public record BalanceResponse(BigDecimal balance) {
}
