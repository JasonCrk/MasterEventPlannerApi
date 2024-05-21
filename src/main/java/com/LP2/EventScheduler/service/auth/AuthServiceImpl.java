package com.LP2.EventScheduler.service.auth;

import com.LP2.EventScheduler.dto.auth.LoginDTO;
import com.LP2.EventScheduler.dto.auth.RegisterDTO;
import com.LP2.EventScheduler.exception.InvalidJwtException;
import com.LP2.EventScheduler.exception.NotAuthenticatedException;
import com.LP2.EventScheduler.model.Account;
import com.LP2.EventScheduler.model.Token;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.model.enums.Role;
import com.LP2.EventScheduler.model.enums.TokenType;
import com.LP2.EventScheduler.repository.AccountRepository;
import com.LP2.EventScheduler.repository.TokenRepository;
import com.LP2.EventScheduler.repository.UserRepository;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.auth.JwtResponse;
import com.LP2.EventScheduler.response.user.SimpleUserResponse;
import com.LP2.EventScheduler.response.user.UserMapper;
import com.LP2.EventScheduler.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public SimpleUserResponse retrieveUserByToken(User authUser) {
        return UserMapper.INSTANCE.toSimpleResponse(authUser);
    }

    @Override
    public JwtResponse register(RegisterDTO registerData) {
        var account = new Account();

        var savedAccount = this.accountRepository.save(account);

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
    public JwtResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new NotAuthenticatedException();

        refreshToken = authHeader.substring(7);

        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null)
            throw new InvalidJwtException("The authentication token is invalid");

        var user = this.userRepository.findByEmail(userEmail)
                .orElseThrow();

        if (!jwtService.isTokenValid(refreshToken, user))
            throw new InvalidJwtException("The refresh token is invalid");

        var accessToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authHeader("Bearer")
                .build();
    }

    @Override
    public MessageResponse verifyToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        final String accessToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new NotAuthenticatedException();

        accessToken = authHeader.substring(7);

        userEmail = jwtService.extractUsername(accessToken);

        if (userEmail == null)
            throw new InvalidJwtException("Token is invalid");

        var user = this.userRepository.findByEmail(userEmail)
                .orElseThrow();

        if (!jwtService.isTokenValid(accessToken, user))
            throw new InvalidJwtException("Token is invalid");

        return new MessageResponse("Token is valid");
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
