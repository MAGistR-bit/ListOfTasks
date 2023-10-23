package com.mag.taskList.web.mappers;

import com.mag.taskList.web.dto.task.TaskDTO;
import com.mag.taskList.domain.task.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    /**
     * Переводит объект Task в TaskDTO
     * @param task задача пользователя
     * @return объект TaskDTO
     */
    TaskDTO toDto (Task task);

    /**
     * Превращает список задач в список DTO
     * @param tasks список задач
     * @return список DTO
     */
    List<TaskDTO> toDto(List<Task> tasks);

    /**
     * Превращает объект TaskDTO в Task
     * @param dto объект TaskDTO
     * @return объект Task
     */
    Task toEntity(TaskDTO dto);
}
