package com.mag.taskList.web.security.expression;

import com.mag.taskList.domain.user.Role;
import com.mag.taskList.service.UserService;
import com.mag.taskList.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Класс, в котором описываются Security Expressions
 */
@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    /**
     * Метод, который проверяет, может ли авторизованный
     * пользователь обращаться к endpoint'ам другого пользователя
     * @param id идентификатор пользователя, endpoint'ы которого,
     *           хочется получить
     * @return true - пользователь имеет доступ, false - пользователь НЕ
     * имеет доступ
     */
    public boolean canAccessUser(Long id) {
        // Получить данные об аутентификации пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Объект JwtEntity хранит данные, необходимые для проверки
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();

        // Возвратим true, если авторизованный пользователь
        // попадает в свой endpoint, либо является администратором
        return userId.equals(id) || hasAnyRole(authentication, Role.ROLE_ADMIN);

    }

    /**
     * Метод, который проверяет, является ли пользователем администратором
     * @param authentication объект {@link Authentication}, содержащий данные об аутентификации пользователя
     * @param roles список ролей
     * @return true - пользователь является администратором,
     * false - пользователь НЕ является администратором
     */
    private boolean hasAnyRole(Authentication authentication, Role... roles) {
        for (Role role : roles) {
            // Создать SimpleGrantedAuthority с ролью ADMIN
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());

            // Проверить, обладает ли пользователь ролью администратора
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод, который проверяет, имеет ли авторизованный пользователь
     * доступ к определенной задаче
     * @param taskId задача, которую хотелось бы получить
     * @return true - пользователь имеет доступ к задаче,
     * false - пользователь НЕ имеет доступ к задаче
     */
    public boolean canAccessTask(Long taskId) {
        // Получить данные об аутентификации пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Объект JwtEntity хранит данные, необходимые для проверки
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();

        return userService.isTaskOwner(userId, taskId);
    }

}
