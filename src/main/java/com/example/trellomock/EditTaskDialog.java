package com.example.trellomock;

import com.example.trellomock.task.Task;
import com.example.trellomock.ScheduledTasks;
import com.example.trellomock.taskCategory.TaskCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditTaskDialog {
    @FXML
    private ChoiceBox RecurSelector;
    @FXML
    private javafx.scene.control.ColorPicker colorPicker;
    Task editingTask;
    @FXML
    private javafx.scene.control.DatePicker datePicker;
    @FXML
    private TextField descriptionTextField;

    @FXML
    private Button createTaskButton;

    public EditTaskDialog(){
    }

    private static final Logger logger = LoggerFactory.getLogger(EditTaskDialog.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

   private static final TaskCategory tc = new TaskCategory(1L,"tempcat", "Ad Min", "tempcat"); //TODO Use an actual category
 //TODO Use an actual category
    private ObservableList<Task> appMainObservableList;

    public void setAppMainObservableList(ObservableList<Task> tvObservableList) {
        this.appMainObservableList = tvObservableList;

        editingTask = this.appMainObservableList.get(0);
        descriptionTextField.setText(editingTask.GetDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime dt;
        if(editingTask.GetDueDate()!=null)
            dt = LocalDateTime.parse(editingTask.GetDueDate(),formatter);
        else {
            dt = LocalDateTime.parse(editingTask.GetCreatedOn(),formatter);
        }
        datePicker.setValue(dt.toLocalDate());
        RecurSelector.setItems(FXCollections.observableArrayList("None","Every Ten Seconds","Every Day", "Every other day","Every Week"));
    }


    public void HandleCancelAddTask(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }
    public void HandleRecurSelected(ActionEvent actionEvent) {
       this.editingTask.SetRecurrenceType(RecurSelector.getValue().toString());
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public void HandleChooseDate(ActionEvent actionEvent) {
        LocalDate date = datePicker.getValue();
        System.out.println("Selected date: " + date.toString() + editingTask.getScheduleDate().toString().substring(10));
        editingTask.SetDueDate(date.toString() + editingTask.getScheduleDate().toString().substring(9));
    }
//2020-11-25 22:42:13
//2020-11-25 22:44:27
    public void HandleModifyTask(ActionEvent actionEvent) {
        if (descriptionTextField.getText().trim().length()>0) {
            String description = descriptionTextField.getText().trim();
            if(datePicker.getValue()!=null) {
                editingTask.SetDueDate(datePicker.getValue().toString());
            }
            if(colorPicker.getValue()!=null) {
                int r = ((int) Math.round(colorPicker.getValue().getRed()     * 255)) << 24;
                int g = ((int) Math.round(colorPicker.getValue().getGreen()   * 255)) << 16;
                int b = ((int) Math.round(colorPicker.getValue().getBlue()    * 255)) << 8;
                int a = ((int) Math.round(colorPicker.getOpacity() * 255));

                String style = String.format("#%08X", (r + g + b + a));
                editingTask.SetColor(style);
            }
            editingTask.SetDescription(description);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            editingTask.setScheduleDate(java.time.LocalDateTime.now().format(formatter));
            appMainObservableList.set(0, editingTask);
        }
        closeStage(actionEvent);
    }

    public void HandleCancelModifyTask(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }
}
