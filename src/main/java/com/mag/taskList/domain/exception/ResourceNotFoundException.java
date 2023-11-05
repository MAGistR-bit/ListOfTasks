package com.mag.taskList.domain.exception;

/**
 * @author mikhail
 * Исключение, которое будет отображено, если
 * ресурс не удалось найти.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Конструктор с параметрами (создание объекта)
     * @param message сообщение, которое необходимо отобразить
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
