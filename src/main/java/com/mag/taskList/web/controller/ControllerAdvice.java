package com.mag.taskList.web.controller;

import com.mag.taskList.domain.exception.ExceptionBody;
import com.mag.taskList.domain.exception.ResourceMappingException;
import com.mag.taskList.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Обработка исключений в контроллерах
 */
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourceNotFound(ResourceNotFoundException e) {
        return new ExceptionBody(e.getMessage());
    }

    /**
     * Ошибка, происходящая на стороне сервера
     * @param e сообщение, которое необходимо отобразить
     * @return объект класса ExceptionBody
     */
    @ExceptionHandler(ResourceMappingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleResourceMapping(ResourceMappingException e) {
        return new ExceptionBody(e.getMessage());
    }

}
