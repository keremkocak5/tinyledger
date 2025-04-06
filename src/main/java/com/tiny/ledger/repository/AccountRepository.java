package com.tiny.ledger.repository;

import com.tiny.ledger.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountRepository {

    private final Map<UUID, Account> database = new ConcurrentHashMap<>();

    public Account saveOrUpdate(Account account) {
        database.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(database.getOrDefault(id, null));
    }

}
