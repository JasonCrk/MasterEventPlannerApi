package com.LP2.EventScheduler.service.auth;

import com.LP2.EventScheduler.dto.auth.LoginDTO;
import com.LP2.EventScheduler.dto.auth.RegisterDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.auth.JwtResponse;
import com.LP2.EventScheduler.response.user.SimpleUserResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    SimpleUserResponse retrieveUserByToken(User authUser);
    JwtResponse register(RegisterDTO registerData);
    JwtResponse login(LoginDTO loginData);
    JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
