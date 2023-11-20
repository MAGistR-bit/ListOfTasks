package com.mag.taskList.repository.impl;

import com.mag.taskList.domain.exception.ResourceMappingException;
import com.mag.taskList.domain.user.Role;
import com.mag.taskList.domain.user.User;
import com.mag.taskList.repository.DataSourceConfig;
import com.mag.taskList.repository.UserRepository;
import com.mag.taskList.repository.mappers.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//@Repository
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
                   ur.role           as user_role_role,
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
                   ur.role           as user_role_role,
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

    /**
     * Находит пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     * @return Optional
     */
    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            // Надо передвигаться вперед и назад (по ResultSet), поэтому используем
            // дополнительные параметры
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while finding " +
                    "user by id.");
        }
    }

    /**
     * Находит пользователя по username
     *
     * @param username имя пользователя
     * @return Optional
     */
    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            // Надо передвигаться вперед и назад (по ResultSet), поэтому используем
            // дополнительные параметры
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while finding " +
                    "user by username.");
        }
    }

    /**
     * Обновляет информацию о пользователе
     *
     * @param user пользователь, который существует в системе
     */
    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);

            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    /**
     * Добавляет нового пользователя
     *
     * @param user новый пользователь
     */
    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                user.setId(rs.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while creating user.");
        }
    }

    /**
     * Назначает пользователю определенную роль
     *
     * @param userId идентификатор пользователя
     * @param role   роль
     */
    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while inserting user role.");
        }
    }

    /**
     * Проверяет, является ли пользователь владельцем задачи
     *
     * @param userId идентификатор пользователя
     * @param taskId идентификатор задачи
     * @return true - пользователь является владельцем,
     * false - пользователь не является владельцем
     */
    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_TASK_OWNER);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);

            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                // Получить значение из 1-й колонки
                return rs.getBoolean(1);
            }

        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while checking " +
                    "if user is task owner...");
        }
    }

    /**
     * Удаляет пользователя по его идентификатору
     * @param id идентификатор пользователя
     */
    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while deleting user.");
        }
    }

}
