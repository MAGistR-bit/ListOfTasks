package com.mag.taskList.domain.user;

import com.mag.taskList.domain.task.Task;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class User {

    private Long id;

    private String name;
    private String username;
    private String password;

    private String passwordConfirmation;

    /**
     * Список ролей
     */
    private Set<Role> roles;
    /**
     * Задачи, которые есть у пользователя
     */
    private List<Task> tasks;
}
