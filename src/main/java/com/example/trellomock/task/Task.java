package com.example.trellomock.task;

import com.example.trellomock.taskCategory.TaskCategory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

@Entity
@Table(name = "tasks")
public class Task extends TimerTask {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long taskID;
    private int state;
    private int priority;
    private int sPoints;
    private boolean overdue;

    private boolean complete;
    private String color;
    private String description;
    private String dueDate;
    private String createdBy;
    private String createdOn;
    private String scheduleDate;
    private String RecurrenceType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taskCategory_id")
    private TaskCategory taskCategory;

    protected Task() {}

    @Override
    public void run() {
        if(!this.complete)
            overdue = true;
        listener.onEvent();
    }

    public Task(Long taskID, int state, String description,TaskCategory category, String createdBy) {
        this.taskID = taskID;
        this.state  = state;
        this.description = description;
        this.taskCategory = category;
        this.priority = 4;
        this.createdBy = createdBy;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.createdOn = java.time.LocalDateTime.now().format(formatter);
        this.RecurrenceType = "None";
        this.complete = false;
        this.scheduleDate = createdOn;
        this.overdue = false;
    }

    @Override
    public String toString() {
        if (taskCategory == null) {
            return String.format(
                    "Task[taskID=%d, description='%s', priority='%d', state='%d']",
                    taskID, description, priority, state);
        }

        return String.format(
                "Task[taskID=%d, description='%s', priority='%d', state='%d', category='%s']",
                taskID, description, priority, state, taskCategory.getCategoryName());
    }
    TaskUpdateService listener;
    public void register(TaskUpdateService listener){
        this.listener = listener;
    }
    public void eventHappens(){

    }
    public int GetPriority() {return priority;}

    public int GetState() { return state; }

    public String GetColor() {return this.color;}

    public Long GetTaskID() {return taskID;}

    public int GetSPoints() {return sPoints;}

    public boolean getComplete() {return this.complete;}

    public int getPriority() {return this.priority;}

    public String GetDescription() { return description; }

    public String GetDueDate() { return dueDate; }

    public TaskCategory getTaskCategory() { return taskCategory; }

    public String GetCreatedOn(){return this.createdOn;}

    public String getScheduleDate(){return this.scheduleDate;}

    public String GetCreatedBy() { return this.createdBy; }

    public void SetStoryPoints (int sPoints){this.sPoints = sPoints;}

    public void SetColor(String color){this.color = color;}

    public void SetState(int state){this.state = state;}

    public void SetDescription(String desc){this.description = desc;}

    public void SetDueDate(String dueDate){
        this.dueDate = dueDate;
    }

    public void SetRecurrenceType(String RecurrenceType){this.RecurrenceType = RecurrenceType;}

    public String GetRecurrenceType(){ return this.RecurrenceType;}

    public void setTaskCategory(TaskCategory taskCategory) { this.taskCategory = taskCategory; }

    public void setComplete(boolean c) {this.complete = c;}

    public void setScheduleDate(String sd){this.scheduleDate = sd;}

    public void setPriority(int priority) {this.priority = priority;}

    public boolean GetOverdue() {return this.overdue;}
}
