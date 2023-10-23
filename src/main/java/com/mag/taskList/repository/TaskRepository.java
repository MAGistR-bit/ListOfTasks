package com.mag.taskList.repository;

import com.mag.taskList.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    /**
     * Находит по идентификатору определенную задачу
     * @param id идентификатор задачи
     * @return задача
     */
    Optional<Task> findById(Long id);

    /**
     * Получает список задач, закрепленных за определенным пользователем
     * @param id идентификатор пользователя
     * @return список задач
     */
    List<Task> findAllByUserId(Long id);

    /**
     * Закрепляет задачу за определенным пользователем
     * @param taskId идентификатор задачу
     * @param userId идентификатор пользователя
     */
    void assignToUserById(Long taskId, Long userId);

    void update(Task task);

    void create(Task task);

    void delete(Long id);
}
