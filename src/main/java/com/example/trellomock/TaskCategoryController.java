package com.example.trellomock;

import com.example.trellomock.member.MemberRepository;
import com.example.trellomock.task.Task;
import com.example.trellomock.task.TaskRepository;
import com.example.trellomock.taskCategory.TaskCategory;
import com.example.trellomock.taskCategory.TaskCategoryRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaskCategoryController implements DialogController, Initializable {

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public TaskCategoryController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskCategoryRepository taskCategoryRepository;

    private ArrayList<Long> taskCategories = new ArrayList<Long>();
    private ArrayList<Long> tasks = new ArrayList<Long>();

    @FXML private ListView categoryList;
    @FXML private ListView taskList;
    @FXML private ListView detailList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryList.getItems().clear();
        refresh();
    }

    public void refresh() {
        taskCategories.clear();
        categoryList.getItems().clear();
        taskList.getItems().clear();
        detailList.getItems().clear();

        for (TaskCategory cat : taskCategoryRepository.findAll()) {
            taskCategories.add(cat.getId());
        }

        TaskCategory taskCategory;
        for (int i = 0; i < taskCategories.size(); i++) {
            taskCategory = taskCategoryRepository.findById(taskCategories.get(i)).get();
            categoryList.getItems().add("Category " + taskCategory.getId() + ": " + taskCategory.getCategoryName() +
                    "\nDescription: " + taskCategory.getCategoryDescription() +
                    "\nCreated By: " + taskCategory.getCreatedBy() + "\nCreated On: " + taskCategory.getCreatedOn());
        }
    }

    @FXML
    public void handleCategoryClick(MouseEvent mouseEvent) {
        taskList.getItems().clear();

        Long catId = Long.parseLong(categoryList.getSelectionModel().getSelectedItem().toString().split("")[9]);
        TaskCategory taskCategory = taskCategoryRepository.findById(catId).get();
        Object[] tasks = taskCategory.getTasks().toArray();
        Task task;

        for (int i = 0; i < tasks.length; i++) {
            task = (Task) tasks[i];
            taskList.getItems().add(task.GetDescription());
        }

        if (mouseEvent.getClickCount() == 2) {
            screens.editTaskCategoryController().clickedCategoryID(catId);
            screens.editTaskCategoryDialog().show();
            screens.editTaskCategoryController().refresh();
        }
    }

    @FXML
    public void handleTaskClick(MouseEvent mouseEvent) {
        detailList.getItems().clear();

        String taskDescription = taskList.getSelectionModel().getSelectedItem().toString();
        Task task = null;

        for (Task t : taskRepository.findAll()) {
            if (t.GetDescription().equals(taskDescription))
                task = t;
        }

        detailList.getItems().add("ID: " + task.GetTaskID());
        detailList.getItems().add("Description: " + task.GetDescription());
        detailList.getItems().add("Story Points: " + task.GetSPoints());
        if (task.GetDueDate() == null)
            detailList.getItems().add("Due Date: No deadline");
        else
            detailList.getItems().add("Due Date: " + task.GetDueDate());
        switch(task.GetState())
        {
            case 0: detailList.getItems().add("State: Backlog"); break;
            case 1: detailList.getItems().add("State: To Do"); break;
            case 2: detailList.getItems().add("State: Doing"); break;
            case 3: detailList.getItems().add("State: Testing"); break;
            case 4: detailList.getItems().add("State: Done"); break;
        }
        detailList.getItems().add("Created By: " + task.GetCreatedBy());
        detailList.getItems().add("Created On: " + task.GetCreatedOn());
    }

    private ObservableList<TaskCategory> taskCategoryObservableList = FXCollections.observableArrayList();
    @FXML
    public void HandleAddTaskCategoryClicked(ActionEvent event) throws Exception {
        screens.addTaskCategoryDialog().show();
    }

    @FXML
    public void clickBackButton() {
        dialog.close();
        screens.boardDialog().show();
    }
}