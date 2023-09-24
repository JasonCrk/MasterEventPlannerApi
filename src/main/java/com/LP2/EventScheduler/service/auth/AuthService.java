package com.LP2.EventScheduler.service.auth;

import com.LP2.EventScheduler.dto.auth.LoginDTO;
import com.LP2.EventScheduler.dto.auth.RegisterDTO;
import com.LP2.EventScheduler.response.auth.JwtResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    JwtResponse register(RegisterDTO registerData);
    JwtResponse login(LoginDTO loginData);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
