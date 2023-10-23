package com.mag.taskList.service;

import com.mag.taskList.domain.task.Task;

import java.util.List;

public interface TaskService {

    /**
     * Получает задачу по определенному идентификатору
     * @param id идентификатор задачи
     * @return задача, закрепленная за пользователем
     */
    Task getById(Long id);

    /**
     * Получает все задачи, закрепленные за пользователем
     * @param id идентификатор пользователя
     * @return список задач
     */
    List<Task> getAllByUserId(Long id);

    Task update(Task task);

    Task create(Task task, Long id);

    void delete(Long id);


}
