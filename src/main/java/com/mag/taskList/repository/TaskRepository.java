package com.mag.taskList.repository;

import com.mag.taskList.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    /**
     * Находит по идентификатору определенную задачу
     *
     * @param id идентификатор задачи
     * @return задача
     */
    Optional<Task> findById(Long id);

    /**
     * Получает список задач, закрепленных за определенным пользователем
     *
     * @param id идентификатор пользователя
     * @return список задач
     */
    List<Task> findAllByUserId(Long id);

    /**
     * Закрепляет задачу за определенным пользователем
     *
     * @param taskId идентификатор задачи
     * @param userId идентификатор пользователя
     */
    void assignToUserById(Long taskId, Long userId);

    /**
     * Обновляет задачу (редактирование задачи)
     *
     * @param task задача, которая ожидает обновление в системе
     */
    void update(Task task);

    /**
     * Создает новую задачу
     *
     * @param task новая задача
     */
    void create(Task task);

    /**
     * Удаляет задачу
     *
     * @param id идентификатор задачи, которую следует удалить
     */
    void delete(Long id);
}
