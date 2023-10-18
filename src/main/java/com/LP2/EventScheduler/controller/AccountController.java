package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.dto.account.UpdateAccountDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.service.account.AccountService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PatchMapping
    public ResponseEntity<MessageResponse> updateAccount(
            @Valid UpdateAccountDTO accountData,
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.accountService.updateAccount(accountData, authUser));
    }
}
