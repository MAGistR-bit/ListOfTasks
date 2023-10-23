package com.mag.taskList.service;

import com.mag.taskList.domain.user.User;

public interface UserService {
    /**
     * Получить пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return пользователь
     */
    User getById(Long id);

    /**
     * Получить пользователя
     * @param username имя пользователя
     * @return пользователь
     */
    User getByUsername(String username);

    User update(User user);

    User create(User user);

    /**
     * Проверяем, что задача закреплена за
     * определенным пользователем
     * @param userId идентификатор пользователя
     * @param taskId идентификатор задачи
     * @return true или false
     */
    boolean isTaskOwner(Long userId, Long taskId);

    void delete(Long id);
}
