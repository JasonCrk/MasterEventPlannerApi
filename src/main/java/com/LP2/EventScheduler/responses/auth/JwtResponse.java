package com.LP2.EventScheduler.responses.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String authHeader;
}
