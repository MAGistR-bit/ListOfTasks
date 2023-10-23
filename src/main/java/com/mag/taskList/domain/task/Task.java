package com.mag.taskList.domain.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private Long id;
    /**
     * Заголовок
     */
    private String title;
    /**
     * Описание задачи
     */
    private String description;
    /**
     * Статус задачи
     */
    private Status status;
    /**
     * Дата, когда задача должна быть выполнена
     */
    private LocalDateTime expirationDate;

}
