package com.mag.taskList.service.impl;

import com.mag.taskList.domain.exception.ResourceNotFoundException;
import com.mag.taskList.domain.task.Status;
import com.mag.taskList.domain.task.Task;
import com.mag.taskList.repository.TaskRepository;
import com.mag.taskList.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    /**
     * Получает задачу по идентификатору.
     * Если задача отсутствует, отображается исключение
     * {@link ResourceNotFoundException}.
     * Помимо этого, метод работает с Redis cache
     * (получает записи).
     *
     * @param id идентификатор задачи
     * @return задача, созданная пользователем
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    /**
     * Получает список задач, закрепленных за пользователем
     *
     * @param id идентификатор пользователя
     * @return список задач
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long id) {
        return taskRepository.findAllByUserId(id);
    }

    /**
     * Обновляет задачу (редактирование задачи)
     *
     * @param task задача, которую необходимо обновить
     * @return задача
     */
    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            // Установить статус задачи
            task.setStatus(Status.TODO);
        }
        // Обновить задачу
        taskRepository.update(task);

        return task;
    }

    /**
     * Создает новую задачу. Вдобавок к этому, метод
     * добавляет новую задачу в кэш.
     *
     * @param task   задача, которую необходимо создать
     * @param userId идентификатор пользователя, который
     *               используется для назначения задачи
     *               определенному пользователю
     * @return задача, созданная в системе
     */
    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById", key = "#task.id")
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);
        // Создать задачу
        taskRepository.create(task);
        // Закрепить задачу за пользователем
        taskRepository.assignToUserById(task.getId(), userId);
        return task;
    }


    /**
     * Удаляет задачу
     *
     * @param id идентификатор задачи. Этот параметр
     *           используется для удаления определенной задачи.
     */
    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.delete(id);
    }

}
