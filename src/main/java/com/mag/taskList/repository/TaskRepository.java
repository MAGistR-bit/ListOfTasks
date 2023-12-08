package com.mag.taskList.repository;

import com.mag.taskList.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {


    /**
     * Получает все задачи, закрепленные за определенным пользователем.
     * Hibernate самостоятельно преобразует результат в список задач.
     *
     * @param userId идентификатор пользователя
     * @return список задач
     */
    @Query(value = """
            SELECT * FROM tasks t
            JOIN users_tasks ut ON ut.task_id = t.id
            WHERE ut.user_id = :userId
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);

    /* Методы update и create заменены на save.
    * Метод findById имеется у JpaRepository. */


}
