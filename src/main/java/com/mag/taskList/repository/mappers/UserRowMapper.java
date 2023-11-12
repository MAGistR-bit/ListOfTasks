package com.mag.taskList.repository.mappers;

import com.mag.taskList.domain.task.Task;
import com.mag.taskList.domain.user.Role;
import com.mag.taskList.domain.user.User;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRowMapper {

    /**
     * Преобразует значение из результирующего набора
     * данных в объект User
     *
     * @param resultSet результирующий набор данных
     * @return пользователь, либо null, если пользователь не найден
     */
    @SneakyThrows
    public static User mapRow(ResultSet resultSet) {
        // Создать список ролей
        Set<Role> roles = new HashSet<>();

        // Получить роли
        while (resultSet.next()) {
            // Получаем значение из определенного столбца
            roles.add(Role.valueOf(resultSet.getString("user_role_role")));
        }

        // Возвращение в начало
        resultSet.beforeFirst();
        List<Task> tasks = TaskRowMapper.mapRows(resultSet);
        resultSet.beforeFirst();

        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setName(resultSet.getString("user_name"));
            user.setUsername(resultSet.getString("user_username"));
            user.setPassword(resultSet.getString("user_password"));
            // Добавить роли и задачи
            user.setRoles(roles);
            user.setTasks(tasks);
            return user;
        }

        return null;
    }
}
