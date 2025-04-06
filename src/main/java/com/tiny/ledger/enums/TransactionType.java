package com.tiny.ledger.enums;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum TransactionType {

    DEPOSIT((a, b) -> a.add(b)),
    WITHDRAW((a, b) -> a.subtract(b));

    @Getter
    private BiFunction<BigDecimal, BigDecimal, BigDecimal> transactionOperation;

    TransactionType(BiFunction<BigDecimal, BigDecimal, BigDecimal> transactionOperation) {
        this.transactionOperation = transactionOperation;
    }

}
