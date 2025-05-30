package com.tiny.ledger.model;

import com.tiny.ledger.enums.TransactionType;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DisabledIf(expression = "#{environment.acceptsProfiles('prod')}", reason = "Test should not run in production due to performance.")
@SpringBootTest
class AccountThreadSafetyTest {

    private final int ITERATION_COUNT = 2_000_000;

    @Test
    void addTransactionIfBalancePositiveShouldBeThreadSafe() throws InterruptedException {
        Account account = new Account(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "Kerem Kocak", BigDecimal.ZERO, "GBP", new LinkedList<>(), Date.from(Instant.now()));
        TransactionCreator transactionCreator = new TransactionCreator(account);
        Thread thread1 = new Thread(transactionCreator);
        Thread thread2 = new Thread(transactionCreator);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        assertThat(account.getBalance(), is(BigDecimal.valueOf(ITERATION_COUNT * 2).setScale(2, RoundingMode.HALF_UP)));
        assertThat(account.getTransactions().size(), is(ITERATION_COUNT * 2));
    }

    @AllArgsConstructor
    public class TransactionCreator implements Runnable {

        private final Account sharedAccount;

        @Override
        public void run() {
            for (int i = 0; i < ITERATION_COUNT; i++) {
                sharedAccount.addTransactionIfBalancePositive(BigDecimal.ONE, TransactionType.DEPOSIT);
            }
        }
    }

}