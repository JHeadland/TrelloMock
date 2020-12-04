package com.example.trellomock;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class ScreensConfiguration {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showScreen(Parent screen) {
        primaryStage.setScene(new Scene(screen, 1280, 800));
        primaryStage.setTitle("Trello Mock");
        primaryStage.show();
    }

    @Bean
    FXMLDialog loginDialog() {
        return new FXMLDialog(loginController(), getClass().getResource("/Login.fxml"), primaryStage);
    }

    @Bean
    LoginController loginController() {
        return new LoginController(this);
    }

    @Bean
    FXMLDialog registerDialog() {
        return new FXMLDialog(registerController(), getClass().getResource("/Register.fxml"), primaryStage);
    }

    @Bean
    RegisterController registerController() {
        return new RegisterController(this);
    }

    @Bean
    FXMLDialog boardDialog() {
        return new FXMLDialog(boardController(), getClass().getResource("/Board.fxml"), primaryStage);
    }

    @Bean
    BoardController boardController() {
        return new BoardController(this);
    }

    @Bean
    FXMLDialog memberSettingsDialog() {
        return new FXMLDialog(memberSettingsController(), getClass().getResource("/MemberSettings.fxml"), primaryStage);
    }

    @Bean
    FXMLDialog GetTaskDialog() {
        return new FXMLDialog( addTaskDialog(), getClass().getResource("/AddTaskDialog.fxml"), primaryStage);
    }

    @Bean
    AddTaskDialog addTaskDialog() {
        return new AddTaskDialog();
    }

    @Bean
    MemberSettingsController memberSettingsController() {
        return new MemberSettingsController(this);
    }

    @Bean
    FXMLDialog teamSettingsDialog() {
        return new FXMLDialog(teamSettingsController(), getClass().getResource("/TeamSettings.fxml"), primaryStage);
    }

    @Bean
    TeamSettingsController teamSettingsController() {
        return new TeamSettingsController(this);
    }

    @Bean
    FXMLDialog addTeamDialog() {
        return new FXMLDialog(addTeamController(), getClass().getResource("/addTeam.fxml"), primaryStage);
    }

    @Bean
    AddTeamController addTeamController() {
        return new AddTeamController(this);
    }

    @Bean
    FXMLDialog taskCategoryDialog() {
        return new FXMLDialog(taskCategoryController(), getClass().getResource("/TaskCategory.fxml"), primaryStage);
    }

    @Bean
    TaskCategoryController taskCategoryController() {
        return new TaskCategoryController(this);
    }

    @Bean
    FXMLDialog addTaskCategoryDialog() {
        return new FXMLDialog(addTaskCategoryController(), getClass().getResource("/AddTaskCategory.fxml"), primaryStage);
    }

    @Bean
    AddTaskCategoryController addTaskCategoryController() {
        return new AddTaskCategoryController(this);
    }

    @Bean
    FXMLDialog editTaskCategoryDialog() {
        return new FXMLDialog(editTaskCategoryController(), getClass().getResource("/EditTaskCategory.fxml"), primaryStage);
    }

    @Bean
    EditTaskCategoryController editTaskCategoryController() {
        return new EditTaskCategoryController(this);
    }

    @Bean
    FXMLDialog adminDialog() {
        return new FXMLDialog(adminController(), getClass().getResource("/Admin.fxml"), primaryStage);
    }

    @Bean
    AdminController adminController() {
        return new AdminController(this);
    }

}
