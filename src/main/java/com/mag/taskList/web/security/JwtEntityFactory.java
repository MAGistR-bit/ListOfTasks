package com.mag.taskList.web.security;

import com.mag.taskList.domain.user.Role;
import com.mag.taskList.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация паттерна "Фабрика"
 */
public class JwtEntityFactory {

    public static JwtEntity create(User user) {
        return new JwtEntity(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                // Роли хранятся в Set
                mapToGrantedAuthorities(new ArrayList<>(user.getRoles()))
        );
    }

    /**
     * Преобразует список ролей (Enums).
     * Задает логику для Spring.
     * @param roles список ролей пользователя
     * @return List<GrantedAuthority> список, который необходим для работы Spring
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles){

        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
