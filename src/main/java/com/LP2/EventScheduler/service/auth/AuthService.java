package com.LP2.EventScheduler.service.auth;

import com.LP2.EventScheduler.dto.LoginDTO;
import com.LP2.EventScheduler.dto.RegisterDTO;
import com.LP2.EventScheduler.responses.auth.JwtResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    JwtResponse register(RegisterDTO registerData);
    JwtResponse login(LoginDTO loginData);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
