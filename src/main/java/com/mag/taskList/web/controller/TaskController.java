package com.mag.taskList.web.controller;

import com.mag.taskList.domain.task.Task;
import com.mag.taskList.service.TaskService;
import com.mag.taskList.web.dto.task.TaskDTO;
import com.mag.taskList.web.dto.validation.OnUpdate;
import com.mag.taskList.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
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
    @Operation(summary = "Update task")
    @PreAuthorize("canAccessTask(#dto.id)")
    public TaskDTO update(@Validated(OnUpdate.class) @RequestBody TaskDTO dto) {
        Task task = taskMapper.toEntity(dto);
        // Обновление задачи на сервере
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get TaskDTO by id")
    @PreAuthorize("canAccessTask(#id)")
    public TaskDTO getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    @PreAuthorize("canAccessTask(#id)")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }


}
