package com.LP2.EventScheduler.response.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPictureResponse {
    private UUID id;
    private String picture;
}
