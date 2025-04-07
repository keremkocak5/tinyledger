package com.tiny.ledger.controller.v1;

import com.tiny.ledger.controller.v1.dto.incoming.AccountRequest;
import com.tiny.ledger.controller.v1.dto.outgoing.AccountResponse;
import com.tiny.ledger.controller.v1.dto.outgoing.BalanceResponse;
import com.tiny.ledger.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final IAccountService accountService;

    @GetMapping(value = "/id/{accountId}/balance")
    @Operation(summary = "Get balance of an account")
    public BalanceResponse getBalance(@PathVariable @NonNull UUID accountId) {
        return accountService.getBalance(accountId);
    }

    @PostMapping
    @Operation(summary = "Create an account")
    public AccountResponse createAccount(@RequestBody @Valid @NonNull AccountRequest accountRequest) {
        return accountService.createAccount(accountRequest);
    }

}
