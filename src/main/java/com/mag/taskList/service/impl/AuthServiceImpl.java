package com.mag.taskList.service.impl;

import com.mag.taskList.domain.user.User;
import com.mag.taskList.service.AuthService;
import com.mag.taskList.service.UserService;
import com.mag.taskList.web.dto.auth.JwtRequest;
import com.mag.taskList.web.dto.auth.JwtResponse;
import com.mag.taskList.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /**
     * {@link AuthenticationManager} используется для авторизации пользователя
     */
    private final AuthenticationManager authenticationManager;
    /**
     * Используется для получения пользователя
     */
    private final UserService userService;
    /**
     * {@link JwtTokenProvider} используется для создания токенов
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Реализация метода, отвечающего за регистрацию пользователя
     * @param loginRequest объект, который содержит username, password
     * @return объект класса JwtResponse
     */
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        // Создать объект
        JwtResponse jwtResponse = new JwtResponse();

        // Перенаправление на метод loadUserByUsername
        // (см. класс JwtUserDetailsService)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (loginRequest.getUsername(),
                        loginRequest.getPassword()
                        )
        );

        // Получить пользователя
        User user = userService.getByUsername(loginRequest.getUsername());

        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(
                jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles())
        );

        jwtResponse.setRefreshToken(
                jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername())
        );

        return jwtResponse;
    }

    /**
     * Реализация метода, отвечающего за обновление refresh_token
     * @param refreshToken токен
     * @return объект класса JwtResponse
     */
    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

}
