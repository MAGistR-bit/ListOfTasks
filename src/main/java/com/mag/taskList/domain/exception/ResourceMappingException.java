package com.mag.taskList.domain.exception;

/**
 * Исключение, которое будет отображаться, когда возникнут ошибки,
 * связанные с JDBC
 */
public class ResourceMappingException extends RuntimeException {
    public ResourceMappingException(String message) {
        super(message);
    }
}
