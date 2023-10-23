package com.mag.taskList.service.impl;

import com.mag.taskList.service.AuthService;
import com.mag.taskList.web.dto.auth.JwtRequest;
import com.mag.taskList.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }

}
