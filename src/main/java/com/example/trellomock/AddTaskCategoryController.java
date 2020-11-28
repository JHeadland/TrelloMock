package com.example.trellomock;

import com.example.trellomock.member.Member;
import com.example.trellomock.member.MemberRepository;
import com.example.trellomock.taskCategory.TaskCategory;
import com.example.trellomock.taskCategory.TaskCategoryRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

public class AddTaskCategoryController implements DialogController {
    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public AddTaskCategoryController(ScreensConfiguration screens) {
        this.screens = screens;
    }


    @FXML private TextField nameTextField;
    @FXML private TextField descriptionTextField;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TaskCategoryRepository taskCategoryRepository;

    @FXML
    public void HandleAddCategory(ActionEvent event) {
        if (nameTextField.getText().trim().length()>0 && descriptionTextField.getText().trim().length()>0) {
            String name = nameTextField.getText().trim();
            String description = descriptionTextField.getText().trim();
            Member creator = memberRepository.findByLogged(true);
            Long tcId = 0L;

            for (TaskCategory taskCategory : taskCategoryRepository.findAll()) {
                tcId = taskCategory.getId();
            }

            TaskCategory tc = new TaskCategory(tcId+1L, name, creator.getFirstName()+" "+creator.getLastName(), java.time.LocalDate.now().toString());
            tc.setCategoryDescription(description);
            taskCategoryRepository.save(tc);
        }

        screens.taskCategoryController().refresh();
        dialog.close();
    }

    @FXML
    public void HandleCancelAddCategory(ActionEvent actionEvent) {
        dialog.close();
    }

    public void onEnter(ActionEvent actionEvent) {
        HandleAddCategory(actionEvent);
    }
}