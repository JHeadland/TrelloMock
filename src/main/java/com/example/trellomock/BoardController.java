package com.example.trellomock;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.trellomock.task.*;
import com.example.trellomock.member.*;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardController implements DialogController, Initializable {

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public BoardController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TaskRepository taskRepository;

    private Member member;
    private ArrayList<Long> tasks;
    private Long currentlySelectedTask;
    private TaskListView selectedList;

    @FXML private TableColumn backlogList;
    @FXML private TableColumn toDoList;
    @FXML private TableColumn doingList;
    @FXML private TableColumn testingList;
    @FXML private TableColumn doneList;
    @FXML private Button deleteTaskButton;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backlogList.setTaskListState(0);
        toDoList.setTaskListState(1);
        doingList.setTaskListState(2);
        testingList.setTaskListState(3);
        doneList.setTaskListState(4);

        member = memberRepository.findByLogged(true);
        tasks = member.getAssignedTasks();

        Task task;
        for(int i = 0; i < tasks.size(); i++)
        {
            task = taskRepository.findById(tasks.get(i)).get();
            switch(task.GetState())
            {
                case 0: backlogList.getItems().add(task); break;
                case 1: toDoList.getItems().add(task); break;
                case 2: doingList.getItems().add(task); break;
                case 3: testingList.getItems().add(task); break;
                case 4: doneList.getItems().add(task); break;
            }

        }
        ArrayList<TaskListView> lists = new ArrayList<>();
        lists.add(backlogList);
        lists.add(toDoList);
        lists.add(doingList);
        lists.add(testingList);
        lists.add(doneList);


        for (TaskListView tlv : lists) {
            tlv.getItems().addListener(new ListChangeListener() {
                @Override
                public void onChanged(ListChangeListener.Change change) {
                    for (int i = 0; i < tlv.getItems().size(); i++) {
                        Task t = tlv.getItems().get(i);
                        taskRepository.save(t);
                        if(tlv.getTaskListState() != t.GetState()) { //in wrong list!
                            tlv.remove(t);
                            System.out.println("in wrong list:");
                            System.out.println(t);
                            switch(t.GetState())
                            {
                                case 0: backlogList.getItems().add(t); break;
                                case 1: toDoList.getItems().add(t); break;
                                case 2: doingList.getItems().add(t); break;
                                case 3: testingList.getItems().add(t); break;
                                case 4: doneList.getItems().add(t); break;
                            }
                        }
                    }
                    for (Task t : taskRepository.findAll()) {
                        System.out.println(t.toString());
                    }
                }
            });
            tlv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
                @Override
                public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
                    if(newValue != null) {
                        System.out.println("Selected item: " + newValue);
                        deleteTaskButton.setVisible(true);
                        deleteTaskButton.setDisable(false);
                        currentlySelectedTask = newValue.GetTaskID();
                        selectedList = tlv;
                    }
                }
            });
            deleteTaskButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent a) {
                    taskRepository.deleteById(currentlySelectedTask);
                    selectedList.getItems().removeIf(t -> t.GetTaskID() == currentlySelectedTask);
                }
            });
        }
    }


    private ObservableList<Task> taskDialogObservableList = FXCollections.observableArrayList();
    @FXML
    public void HandleAddTaskClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddTaskDialog.fxml"));
        Parent parent = fxmlLoader.load();
        AddTaskDialog dialogController = fxmlLoader.<AddTaskDialog>getController();
        dialogController.setAppMainObservableList(taskDialogObservableList);
        dialogController.SetActiveMember(memberRepository.findByLogged(true));

        Scene scene = new Scene(parent, 300, 200);
        Stage stage = new Stage();
        String css = this.getClass().getResource("/AddTaskDialog.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Task");
        stage.setScene(scene);
        stage.showAndWait();

        if(taskDialogObservableList.size()>0){
            Task t = taskDialogObservableList.get(taskDialogObservableList.size()-1);
            backlogList.getItems().add(t);
            taskDialogObservableList.remove(t);
        }
    }

    @FXML
    public void clickMemberSettingsButton() {
        dialog.close();
        screens.memberSettingsDialog().show();
        screens.memberSettingsController().refresh();
    }

    @FXML
    public void clickTeamSettingsButton() {
        dialog.close();
        screens.teamSettingsDialog().show();
    }

    @FXML
    public void taskCategoryButton() {
        dialog.close();
        screens.taskCategoryDialog().show();
    }
}