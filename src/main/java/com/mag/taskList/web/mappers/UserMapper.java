package com.mag.taskList.web.mappers;

import com.mag.taskList.domain.user.User;
import com.mag.taskList.web.dto.user.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Переводит объект User в UserDTO
     * @param user пользователь
     * @return объект UserDTO
     */
    UserDTO toDto (User user);

    /**
     * Переводит объект UserDTO в User
     * @param dto Data Transfer Object (DTO)
     * @return объект User
     */
    User toEntity(UserDTO dto);
}
