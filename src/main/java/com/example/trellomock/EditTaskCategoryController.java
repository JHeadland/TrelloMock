package com.example.trellomock;

import com.example.trellomock.member.Member;
import com.example.trellomock.taskCategory.TaskCategory;
import com.example.trellomock.taskCategory.TaskCategoryRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditTaskCategoryController implements DialogController, Initializable {

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public EditTaskCategoryController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML private TextField nameTextField;
    @FXML private TextField descriptionTextField;
    @FXML private Text deleteWarning;

    @Autowired
    TaskCategoryRepository taskCategoryRepository;

    private TaskCategory taskCategory = null;
    private boolean warningFlag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
    }

    public void refresh() {
        warningFlag = false;
        deleteWarning.setText("");
        nameTextField.setText(taskCategory.getCategoryName());
        descriptionTextField.setText(taskCategory.getCategoryDescription());
    }

    public void clickedCategoryID(Long catId) {
        taskCategory = taskCategoryRepository.findById(catId).get();
    }

    @FXML
    public void clickCancelButton(ActionEvent actionEvent) throws IOException {
        dialog.close();
    }

    @FXML
    public void clickChangeButton(ActionEvent actionEvent) {
        String name = String.valueOf(nameTextField.getText());
        String description = String.valueOf(descriptionTextField.getText());

        if (!(nameTextField.getText() == null || nameTextField.getText().trim().isEmpty()))
            taskCategory.setCategoryName(name);
        if (!(descriptionTextField.getText() == null || descriptionTextField.getText().trim().isEmpty()))
            taskCategory.setCategoryDescription(description);

        taskCategoryRepository.save(taskCategory);

        nameTextField.setText("");
        descriptionTextField.setText("");

        dialog.close();
        screens.taskCategoryController().refresh();
    }

    @FXML
    public void clickDeleteButton(ActionEvent actionEvent) {
        if (!warningFlag) {
            deleteWarning.setText("Deletion is irreversible.\nClick delete again to confirm");
            deleteWarning.setFill(Color.RED);
            warningFlag = true;
        }
        else {
            nameTextField.setText("");
            descriptionTextField.setText("");

            taskCategoryRepository.deleteById(taskCategory.getId());

            screens.taskCategoryController().refresh();
            dialog.close();

            // Test deletion
            for (TaskCategory tc : taskCategoryRepository.findAll())
                System.out.println(tc.getCategoryName());
        }
    }
}