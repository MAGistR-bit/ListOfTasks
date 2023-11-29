package com.mag.taskList.domain.task;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/** В данном классе содержится информация о задаче.
 * Сериализация позволяет преобразовать
 * объект в последовательность байтов, которые
 * затем можно сохранить или передать по сети.
 */
@Data
public class Task implements Serializable {
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
