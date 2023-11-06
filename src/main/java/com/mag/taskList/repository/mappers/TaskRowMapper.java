package com.mag.taskList.repository.mappers;

import com.mag.taskList.domain.task.Status;
import com.mag.taskList.domain.task.Task;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TaskRowMapper {

    /**
     * Преобразует строку ResultSet в объект.
     * Используется аннотация SneakyThrows, поскольку необходимо
     * пробросить исключение на уровень выше
     *
     * @param resultSet результирующий набор данных
     * @return задача (объект класса Task)
     */
    @SneakyThrows
    public static Task mapRow(ResultSet resultSet) {
        // Проверяем, есть ли строка в результирующем наборе
        if (resultSet.next()) {
            // Создать объект (Task)
            Task task = new Task();

            // Установка значений
            task.setId(resultSet.getLong("task_id"));
            task.setTitle(resultSet.getString("task_title"));
            task.setDescription(resultSet.getString("task_description"));
            task.setStatus(Status.valueOf(resultSet.getString("task_status")));

            // Получить дату (может возникнуть NullPointerException)
            Timestamp timestamp = resultSet.getTimestamp("task_expiration_date");
            if (timestamp != null) {
                task.setExpirationDate(timestamp.toLocalDateTime());
            }
            return task;
        }

        return null;
    }


    /**
     * Преобразует строки ResultSet в список задач
     *
     * @param resultSet результирующий набор данных
     * @return список задач
     */
    @SneakyThrows
    public static List<Task> mapRows(ResultSet resultSet) {
        // Создать список задач
        List<Task> tasks = new ArrayList<>();

        // Проверяем, есть ли строка в результирующем наборе
        while (resultSet.next()) {
            Task task = new Task();
            // Установка значений
            task.setId(resultSet.getLong("task_id"));

            if (!resultSet.wasNull()) {

                task.setTitle(resultSet.getString("task_title"));
                task.setDescription(resultSet.getString("task_description"));
                task.setStatus(Status.valueOf(resultSet.getString("task_status")));

                // Получить дату (может возникнуть NullPointerException)
                Timestamp timestamp = resultSet.getTimestamp("task_expiration_date");
                if (timestamp != null) {
                    task.setExpirationDate(timestamp.toLocalDateTime());
                }
                // Добавить задачу в список
                tasks.add(task);
            }
        }

        return tasks;
    }

}
