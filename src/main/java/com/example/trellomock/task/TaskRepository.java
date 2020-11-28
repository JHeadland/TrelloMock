package com.example.trellomock.task;

import java.util.List;

import com.example.trellomock.taskCategory.TaskCategory;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Task findById(long id);
    void deleteById(long id);
    List<Task> findByTaskCategory(TaskCategory taskCategory);
}
