package com.mag.taskList.repository.impl;

import com.mag.taskList.domain.exception.ResourceMappingException;
import com.mag.taskList.domain.task.Task;
import com.mag.taskList.repository.DataSourceConfig;
import com.mag.taskList.repository.TaskRepository;
import com.mag.taskList.repository.mappers.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;

    /**
     * Для выполнения SQL-запросов используется
     * интерфейс PreparedStatement.
     * Данный запрос отображает подробную информацию
     * про задачу
     */
    private final String FIND_BY_ID = """
            SELECT t.id              as task_id,
                   t.title           as task_title,
                   t.description     as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status          as task_status
            FROM tasks t
            WHERE id = ?
            """;

    /**
     * SQL-запрос, отображающий задачи,
     * закрепленные за определенным пользователем
     */
    private final String FIND_ALL_BY_USER_ID = """
            SELECT t.id              as task_id,
                   t.title           as task_title,
                   t.description     as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status          as task_status
            FROM tasks t
                     JOIN tasklist.users_tasks ut on t.id = ut.task_id
            WHERE ut.user_id = ?
            """;

    /**
     * SQL-запрос, позволяющий назначить задачу пользователю
     */
    private final String ASSIGN = """
            INSERT INTO users_tasks (task_id, user_id)
            VALUES (?, ?)
            """;

    /**
     * SQL-запрос, обновляющий данные (задачу)
     */
    private final String UPDATE = """
            UPDATE tasks
            SET title = ?,
                description = ?,
                expiration_date = ?,
                status = ?
            WHERE id = ?
            """;


    /**
     * SQL-запрос, который создает новую задачу
     */
    private final String CREATE = """
            INSERT INTO tasks (title, description, expiration_date, status)
            VALUES (?, ?, ?, ?)
            """;

    /**
     * SQL-запрос, который удаляет определенную задачу
     */
    private final String DELETE = """
            DELETE FROM tasks
            WHERE id = ?
            """;

    /**
     * Отображает информацию про определенную задачу
     *
     * @param id идентификатор задачи
     * @return объект класса {@link Optional}
     */
    @Override
    public Optional<Task> findById(Long id) {
        try {
            // Получить соединение
            Connection connection = dataSourceConfig.getConnection();
            // Создать PreparedStatement
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            // Передать id для подстановки в запрос
            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(TaskRowMapper.mapRow(rs));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while finding " +
                    "user by id.");
        }
    }

    /**
     * Получает задачи, закрепленные за определенным пользователем
     *
     * @param userId идентификатор пользователя
     * @return список задач
     */
    @Override
    public List<Task> findAllByUserId(Long userId) {
        try {
            // Получить соединение
            Connection connection = dataSourceConfig.getConnection();
            // Создать PreparedStatement
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            // Передать id для подстановки в запрос
            statement.setLong(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                return TaskRowMapper.mapRows(rs);
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while finding " +
                    "all by user id.");
        }
    }

    /**
     * Метод, который назначает задачу пользователю
     *
     * @param taskId идентификатор задачи
     * @param userId идентификатор пользователя
     */
    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(ASSIGN);
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while assigning to user.");
        }
    }

    /**
     * Обновляет данные о задаче
     *
     * @param task задача, которая ожидает обновление в системе
     */
    @Override
    public void update(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, task.getTitle());

            // Описание может иметь null, поэтому производим проверку
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }

            // Срок жизни задачи может быть null
            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }

            statement.setString(4, task.getStatus().name());
            statement.setLong(5, task.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while updating task.");
        }
    }

    /**
     * Метод, который создает задачу (добавляет в систему)
     *
     * @param task новая задача
     */
    @Override
    public void create(Task task) {
        try {
            // Получить соединение
            Connection connection = dataSourceConfig.getConnection();
            // Подготовить PreparedStatement
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);

            // Установить значения для statement
            statement.setString(1, task.getTitle());

            // Описание может быть null, поэтому необходимо произвести проверку
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }

            // Срок жизни задачи может быть null
            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }

            statement.setString(4, task.getStatus().name());
            // Используется для выполнения операторов INSERT, UPDATE или DELETE
            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                task.setId(rs.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while creating task.");
        }
    }

    /**
     * Удаляет задачу
     *
     * @param id идентификатор задачи, которую следует удалить
     */
    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while deleting task.");
        }
    }

}
