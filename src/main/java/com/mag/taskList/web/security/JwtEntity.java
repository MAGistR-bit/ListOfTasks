package com.mag.taskList.web.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Хранение пользователя для Spring
 */
@Data
@AllArgsConstructor
public class JwtEntity implements UserDetails {

    /**
     * Идентификатор пользователя
     */
    private Long id;

    /**
     * E-mail пользователя
     */
    private final String username;

    /**
     * Имя пользователя
     */
    private final String name;
    /**
     * Пароль
     */
    private final String password;
    /**
     * Полномочия, которые предоставляются пользователю
     */
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
