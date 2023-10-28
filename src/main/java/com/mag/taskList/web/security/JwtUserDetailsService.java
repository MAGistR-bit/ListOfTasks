package com.mag.taskList.web.security;

import com.mag.taskList.domain.user.User;
import com.mag.taskList.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Класс (сервис), который необходим Spring Security
 * для получения какого-либо пользователя.
 * Spring получает пользователя, используя метод loadUserByUsername
 * @see UserDetailsService
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Позволяет получить из источника данных объект пользователя
     * и сформировать из него объект UserDetails, который будет использоваться
     * контекстом Spring Security.
     * @param username логин пользователя (e-mail)
     * @return объект UserDetails
     * @throws UsernameNotFoundException исключение, которое может возникнуть
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Получить пользователя из базы
        User user = userService.getByUsername(username);
        // Возвратить объект UserDetails, используя фабрику
        return JwtEntityFactory.create(user);
    }
}
