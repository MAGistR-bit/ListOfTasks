package com.mag.taskList.web.controller;

import com.mag.taskList.domain.task.Task;
import com.mag.taskList.domain.user.User;
import com.mag.taskList.service.TaskService;
import com.mag.taskList.service.UserService;
import com.mag.taskList.web.dto.task.TaskDTO;
import com.mag.taskList.web.dto.user.UserDTO;
import com.mag.taskList.web.dto.validation.OnCreate;
import com.mag.taskList.web.dto.validation.OnUpdate;
import com.mag.taskList.web.mappers.TaskMapper;
import com.mag.taskList.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @PutMapping
    @Operation(summary = "Update user")
    public UserDTO update(@Validated(OnUpdate.class) @RequestBody UserDTO dto) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get UserDTO by id")
    public UserDTO getById(@PathVariable Long id) {
        // Получить пользователя
        User user = userService.getById(id);
        // Возвратить соответствующее значение
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }

    /**
     * Получить список задач определенного пользователя
     *
     * @param id идентификатор пользователя
     * @return список задач
     */
    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get all User tasks")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long id) {
        // Получить список задач
        List<Task> tasksList = taskService.getAllByUserId(id);
        // Возвратить соответствующее значение
        return taskMapper.toDto(tasksList);
    }


    @PostMapping("/{id}/tasks")
    @Operation(summary = "Add task to user")
    public TaskDTO createTask(@PathVariable Long id,
                              @Validated(OnCreate.class) @RequestBody TaskDTO dto) {

        // Преобразовать DTO в сущность (Entity)
        Task task = taskMapper.toEntity(dto);
        // Присвоить задачу пользователю
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }

}
