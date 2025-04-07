package com.tiny.ledger.enums;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum TransactionType {

    DEPOSIT(BigDecimal::add),
    WITHDRAW(BigDecimal::subtract);

    @Getter
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> accountBalanceOperator;

    TransactionType(BiFunction<BigDecimal, BigDecimal, BigDecimal> accountBalanceOperator) {
        this.accountBalanceOperator = accountBalanceOperator;
    }

}
