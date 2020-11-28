package com.example.trellomock.taskCategory;

import com.example.trellomock.task.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskCategoryRepository extends CrudRepository<TaskCategory,Long> {
    TaskCategory findById(long Id);
    TaskCategory findBycategoryID(String categoryID);
    TaskCategory findBycategoryName(String categoryName);
}
