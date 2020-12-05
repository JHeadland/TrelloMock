package com.example.trellomock;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.trellomock.task.*;
import com.example.trellomock.taskCategory.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.trellomock.member.*;
import org.springframework.context.annotation.Bean;

public class AddTaskDialog implements DialogController {
    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField stateTextField;

    @FXML
    private Button createTaskButton;

    private static final TaskCategory tc = new TaskCategory(1L, "tempcat", "Ad Min", java.time.LocalDate.now().toString()); //TODO Use an actual category
    private ObservableList<Task> appMainObservableList;
    private Member loggedMember;

    public void setAppMainObservableList(ObservableList<Task> tvObservableList) {
        this.appMainObservableList = tvObservableList;

    }

    public void SetActiveMember(Member member){
        this.loggedMember = member;
    }
    public AddTaskDialog(){}
    @FXML
    public void HandleAddTask(javafx.event.ActionEvent event) {
        if (stateTextField.getText().trim().length()>0 && descriptionTextField.getText().trim().length()>0) {
            int state = Integer.parseInt(stateTextField.getText().trim());
            String description = descriptionTextField.getText().trim();

            Task t = new Task(2L, state, description, tc,loggedMember.getFirstName()); //TODO actual task ID
            appMainObservableList.add(t);
        }
        closeStage(event);
    }
    public void HandleCancelAddTask(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    private void closeStage(javafx.event.ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setDialog(FXMLDialog dialog) {

    }
}