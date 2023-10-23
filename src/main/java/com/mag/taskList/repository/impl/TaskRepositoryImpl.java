package com.mag.taskList.repository.impl;

import com.mag.taskList.domain.task.Task;
import com.mag.taskList.repository.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    @Override
    public Optional<Task> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByUserId(Long id) {
        return null;
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {

    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void create(Task task) {

    }

    @Override
    public void delete(Long id) {

    }
}
