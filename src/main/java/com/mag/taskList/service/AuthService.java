package com.mag.taskList.service;

import com.mag.taskList.web.dto.auth.JwtRequest;
import com.mag.taskList.web.dto.auth.JwtResponse;

/**
 * Сервис для авторизации пользователей
 */
public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
