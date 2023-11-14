package com.mag.taskList.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author mikhail
 * Класс, который будет использоваться
 * при возврате различных исключений
 */
@Data
@AllArgsConstructor
public class ExceptionBody {

    /**
     * Сообщение об ошибке
     */
    private String message;
    /**
     * Ошибки
     */
    private Map<String, String> errors;

    /**
     * Параметризованный конструктор
     *
     * @param message сообщение, которое необходимо отобразить
     */
    public ExceptionBody(String message) {
        this.message = message;
    }
}
