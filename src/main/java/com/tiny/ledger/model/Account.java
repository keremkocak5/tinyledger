package com.tiny.ledger.model;

import com.tiny.ledger.enums.ErrorCode;
import com.tiny.ledger.enums.TransactionType;
import com.tiny.ledger.exception.TinyLedgerRuntimeException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import static com.tiny.ledger.util.Constants.GBP;
import static com.tiny.ledger.util.Constants.ROUNDING_FUNCTION;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Account {

    @NotEmpty
    @NonNull
    private final UUID id;

    @NotEmpty
    @NonNull
    @Size(min = 1, max = 100)
    private final String accountOwnerName;

    @DecimalMin(value = "0.0")
    @NonNull
    private BigDecimal balance;

    @Size(min = 1, max = 4)
    @NonNull
    private final String currencyCode;

    @NonNull
    private final LinkedList<Transaction> transactions;

    @NonNull
    @NotEmpty
    private final Date creationDate;

    public Transaction addTransactionIfBalancePositive(BigDecimal amount, TransactionType transactionType) {
        synchronized (this) {
            amount = ROUNDING_FUNCTION.apply(amount);
            updateBalanceIfPositive(amount, transactionType);
            return addTransaction(amount, transactionType);
        }
    }

    private Transaction addTransaction(BigDecimal amount, TransactionType transactionType) {
        Transaction newTransaction = new Transaction(UUID.randomUUID(), this.id, amount, GBP, transactionType, Date.from(Instant.now()));
        this.transactions.add(newTransaction);
        return newTransaction;
    }

    private void updateBalanceIfPositive(BigDecimal amount, TransactionType transactionType) {
        BigDecimal newBalance = transactionType.getAccountBalanceOperator().apply(this.balance, amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new TinyLedgerRuntimeException(ErrorCode.BALANCE_NEGATIVE);
        }
        this.balance = newBalance;
    }

}
