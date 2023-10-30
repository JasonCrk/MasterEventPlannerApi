package com.LP2.EventScheduler.response.account;

import com.LP2.EventScheduler.response.user.UserUsernameResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails {
    private UUID id;
    private String picture;
    private String banner;
    private String about;
    private UserUsernameResponse user;
    private String invitationSent;
}
