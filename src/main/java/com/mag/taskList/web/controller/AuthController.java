package com.mag.taskList.web.controller;

import com.mag.taskList.domain.user.User;
import com.mag.taskList.service.AuthService;
import com.mag.taskList.service.UserService;
import com.mag.taskList.web.dto.auth.JwtRequest;
import com.mag.taskList.web.dto.auth.JwtResponse;
import com.mag.taskList.web.dto.user.UserDTO;
import com.mag.taskList.web.dto.validation.OnCreate;
import com.mag.taskList.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Обрабатывает запросы по такому пути
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {


    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    /**
     * Метод для входа в систему
     * @param loginRequest объект, который содержит следующие поля: username, password
     * @return {@link JwtResponse}
     */
    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     * Метод для регистрации пользователя в системе
     * @param userDTO объект, который используется для передачи данных.
     *                Этот параметр будет содержать: name, username, password,
     *                passwordConfirmation
     * @return объект класса UserDTO
     */
    @PostMapping("/register")
    public UserDTO register(@Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
        // Преобразовать DTO к сущности User (получить пользователя)
        User user = userMapper.toEntity(userDTO);
        // Зарегистрировать пользователя (добавить в систему)
        User createdUser = userService.create(user);
        // Возвратить DTO
        return userMapper.toDto(createdUser);
    }

    /**
     * Метода для обновления refresh_token
     * @param refreshToken токен, который необходимо обновить
     * @return объект класса JwtResponse
     */
    @PostMapping("/refresh")
    public JwtResponse refresh (@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }

}
