package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.dto.account.UpdateAccountDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.account.AccountDetails;
import com.LP2.EventScheduler.service.account.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Account")
@RestController
@RequestMapping(path = "/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Retrieve account by id")
    @GetMapping(path = "/{accountId}")
    public ResponseEntity<AccountDetails> retrieveAccount(
            @PathVariable("accountId") UUID accountId,
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.accountService.retrieveAccount(accountId, authUser));
    }

    @Operation(summary = "You can update the banner, picture or \"about\" of your account")
    @PatchMapping
    public ResponseEntity<MessageResponse> updateAccount(
            @Valid UpdateAccountDTO accountData,
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.accountService.updateAccount(accountData, authUser));
    }
}
