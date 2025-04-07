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
import lombok.NonNull;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import static com.tiny.ledger.util.Constants.GBP;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Account {

    @NotEmpty
    @NonNull
    private final UUID id;

    @NotEmpty
    @NonNull
    private final String accountOwnerName;

    @DecimalMin(value = "0.0")
    @ThreadSafe
    @NonNull
    private BigDecimal balance;

    @Size(min = 1, max = 4)
    @NotEmpty
    @NonNull
    private final String currencyCode;

    @NonNull
    private final LinkedList<Transaction> transactions;

    @NonNull
    @NotEmpty
    Date creationDate;

    public Transaction addTransactionIfBalancePositive(BigDecimal amount, TransactionType transactionType) {
        synchronized (this) {
            BigDecimal newBalance = transactionType.getAccountBalanceOperator().apply(this.balance, amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new TinyLedgerRuntimeException(ErrorCode.BALANCE_NEGATIVE);
            }
            this.balance = newBalance;
        }
        Transaction newTransaction = new Transaction(UUID.randomUUID(), amount, GBP, transactionType, Date.from(Instant.now()));
        this.transactions.add(newTransaction);
        return newTransaction;
    }
}
