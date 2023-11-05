package com.mag.taskList.repository.impl;

import com.mag.taskList.domain.task.Task;
import com.mag.taskList.repository.DataSourceConfig;
import com.mag.taskList.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;

    /**
     * Для выполнения SQL-запросов используется
     * интерфейс PreparedStatement.
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

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByUserId(Long id) {
        return null;
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {

    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void create(Task task) {

    }

    @Override
    public void delete(Long id) {

    }
}
