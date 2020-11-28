package com.example.trellomock.taskCategory;

import com.example.trellomock.task.Task;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "taskCategories")
public class TaskCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String categoryName;
    private String categoryDescription;
    private String createdBy;
    private String createdOn;
    

    @Column(unique = true)
    private String categoryID;


    @OneToMany(mappedBy = "taskCategory", orphanRemoval = true, fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)

    private Set<Task> tasks;

    public TaskCategory() {}

    public TaskCategory(Long Id, String categoryName, String createdBy, String createdOn) {
        this.Id = Id;
        this.categoryName = categoryName;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }

    public void dismissTask(Task t) {
        this.tasks.remove(t);
    }

    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() { return categoryDescription; }

    public void setCategoryDescription(String categoryDescription) { this.categoryDescription = categoryDescription; }

    public String getCreatedBy() { return createdBy; }

    public String getCreatedOn() { return createdOn; }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
    
    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getcategoryID() {
        return categoryID;
    }

    public void setcategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
