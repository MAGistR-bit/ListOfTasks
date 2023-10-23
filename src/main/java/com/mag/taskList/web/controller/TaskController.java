package com.mag.taskList.web.controller;

import com.mag.taskList.domain.task.Task;
import com.mag.taskList.service.TaskService;
import com.mag.taskList.web.dto.task.TaskDTO;
import com.mag.taskList.web.dto.validation.OnUpdate;
import com.mag.taskList.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    /**
     * Обновляет данные.
     * Метод принимает JSON и проверяет поля, у которых
     * установлены аннотации со значением OnUpdate.class
     *
     * @param dto объект TaskDTO
     * @return TaskDTO
     */
    @PutMapping
    public TaskDTO update(@Validated(OnUpdate.class) @RequestBody TaskDTO dto) {
        Task task = taskMapper.toEntity(dto);
        // Обновление задачи на сервере
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }


}
