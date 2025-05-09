package com.tiny.ledger.repository;

import com.tiny.ledger.model.Account;
import com.tiny.ledger.util.TestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@EnabledIf(expression = "#{environment.acceptsProfiles('local', 'default')}", reason = "Integration test should run only on local environment!")
@SpringBootTest
class AccountRepositoryIntegrationTest {

    @Test
    void repositoryShouldPersistAndRetrieve() {
        AccountRepository accountRepository = new AccountRepository();
        Account savedAccount = accountRepository.saveOrUpdate(TestConstants.NEW_ACCOUNT);
        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());

        assertThat(foundAccount.isPresent(), is(true));
        assertThat(foundAccount.get(), is(TestConstants.NEW_ACCOUNT));
    }

}