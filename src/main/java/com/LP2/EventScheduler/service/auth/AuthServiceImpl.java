package com.LP2.EventScheduler.service.auth;

import com.LP2.EventScheduler.dto.LoginDTO;
import com.LP2.EventScheduler.dto.RegisterDTO;
import com.LP2.EventScheduler.model.Account;
import com.LP2.EventScheduler.model.Token;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.model.enums.Role;
import com.LP2.EventScheduler.model.enums.TokenType;
import com.LP2.EventScheduler.repository.AccountRepository;
import com.LP2.EventScheduler.repository.TokenRepository;
import com.LP2.EventScheduler.repository.UserRepository;
import com.LP2.EventScheduler.responses.auth.JwtResponse;
import com.LP2.EventScheduler.security.JwtService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse register(RegisterDTO registerData) {
        var account = new Account();

        this.accountRepository.save(account);

        var user = User.builder()
                .userName(registerData.getUsername())
                .email(registerData.getEmail())
                .password(passwordEncoder.encode(registerData.getPassword()))
                .account(account)
                .role(Role.USER)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        return JwtResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .authHeader("Bearer")
                .build();
    }

    @Override
    public JwtResponse login(LoginDTO loginData) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.getEmail(),
                        loginData.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginData.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return JwtResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .authHeader("Bearer")
                .build();
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        final String refreshToken;
        final String userEmail;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);

        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                var authResponse = JwtResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .authHeader("Bearer")
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }
}
