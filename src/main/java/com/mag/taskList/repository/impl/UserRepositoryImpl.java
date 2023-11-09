package com.mag.taskList.repository.impl;

import com.mag.taskList.domain.user.Role;
import com.mag.taskList.domain.user.User;
import com.mag.taskList.repository.DataSourceConfig;
import com.mag.taskList.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    /**
     * Внедрение зависимости (DI)
     */
    private final DataSourceConfig dataSourceConfig;

    /**
     * SQL-запрос, отображающий информацию о пользователе
     * (по его идентификатору)
     */
    private final String FIND_BY_ID = """
            SELECT u.id              as user_id,
                   u.name            as user_name,
                   u.username        as user_username,
                   u.password        as user_password,
                   ur.role           as user_role,
                   t.id              as task_id,
                   t.title           as task_title,
                   t.description     as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status          as task_status
            FROM users u
                     LEFT JOIN tasklist.users_roles ur on u.id = ur.user_id
                     LEFT JOIN tasklist.users_tasks ut on u.id = ut.user_id
                     LEFT JOIN tasklist.tasks t on t.id = ut.task_id
            WHERE u.id = ?
            """;

    /**
     * SQL-запрос, отображающий информацию о пользователе
     * (по его username)
     */
    private final String FIND_BY_USERNAME = """
             SELECT u.id             as user_id,
                   u.name            as user_name,
                   u.username        as user_username,
                   u.password        as user_password,
                   ur.role           as user_role,
                   t.id              as task_id,
                   t.title           as task_title,
                   t.description     as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status          as task_status
            FROM users u
                     LEFT JOIN tasklist.users_roles ur on u.id = ur.user_id
                     LEFT JOIN tasklist.users_tasks ut on u.id = ut.user_id
                     LEFT JOIN tasklist.tasks t on t.id = ut.task_id
            WHERE u.username = ?
            """;

    /**
     * SQL-запрос, обновляющий данные о пользователе
     */
    private final String UPDATE = """
            UPDATE users
            SET name = ?,
                username = ?,
                password = ?
            WHERE id = ?
            """;

    /**
     * SQL-запрос, добавляющий нового пользователя
     */
    private final String CREATE = """
            INSERT INTO users (name, username, password)
            VALUES (?, ?, ?)
            """;

    /**
     * SQL-запрос, который назначает пользователю
     * определенную роль
     */
    private final String INSERT_USER_ROLE = """
            INSERT INTO users_roles (user_id, role)
            VALUES (?, ?)
            """;

    /**
     * SQL-запрос, который проверяет,
     * является ли пользователь владельцем задачи
     */
    private final String IS_TASK_OWNER = """
            SELECT exists(SELECT 1
                          FROM users_tasks
                          WHERE user_id = ?
                            AND task_id = ?)
            """;

    /**
     * SQL-запрос, который удаляет пользователя
     */
    private final String DELETE = """
            DELETE FROM users
            WHERE id = ?
            """;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void create(User user) {

    }

    @Override
    public void insertUserRole(Long userId, Role role) {

    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return false;
    }

    @Override
    public void delete(Long id) {

    }
}
