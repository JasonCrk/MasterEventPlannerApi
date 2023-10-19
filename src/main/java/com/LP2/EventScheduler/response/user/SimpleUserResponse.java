package com.LP2.EventScheduler.response.user;

import com.LP2.EventScheduler.response.account.AccountPictureResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserResponse {
    private UUID id;
    private String username;
    private AccountPictureResponse account;
}
