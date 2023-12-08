package com.mag.taskList.repository;

import com.mag.taskList.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Поиск по username. Используется для Spring Security
     *
     * @param username имя пользователя
     * @return пользователь
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверка безопасности. Пользователь не должен
     * получать доступ к задачам, которые закреплены за
     * другими пользователями.
     *
     * @param userId идентификатор пользователя
     * @param taskId идентификатор задачи
     * @return true или false
     */
    @Query(value = """
            SELECT exists(
                          SELECT 1
                          FROM users_tasks
                          WHERE user_id = :userId
                            AND task_id = :taskId)
            """, nativeQuery = true)
    boolean isTaskOwner(@Param("userId") Long userId, @Param("taskId") Long taskId);

}
