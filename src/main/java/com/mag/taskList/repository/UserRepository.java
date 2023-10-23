package com.mag.taskList.repository;

import java.util.Optional;

import com.mag.taskList.domain.user.Role;
import com.mag.taskList.domain.user.User;

public interface UserRepository {

    /**
     * Находит пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return пользователь
     */
    Optional<User> findById(Long id);

    /**
     * Поиск по username. Используется для Spring Security
     * @param username имя пользователя
     * @return пользователь
     */
    Optional<User> findByUsername(String username);

    /**
     * Обновить данные пользователя
     * @param user пользователь, который существует в системе
     */
    void update(User user);

    /**
     * Создать пользователя
     * @param user новый пользователь
     */
    void create(User user);

    /**
     * Добавляет роль пользователю
     * @param userId идентификатор пользователя
     * @param role роль
     */
    void insertUserRole(Long userId, Role role);

    /**
     * Проверка безопасности. Пользователь не должен
     * получать доступ к задачам, которые закреплены за
     * другими пользователями.
     * @param userId идентификатор пользователя
     * @param taskId идентификатор задачи
     * @return true или false
     */
    boolean isTaskOwner(Long userId, Long taskId);

    void delete (Long id);
}
