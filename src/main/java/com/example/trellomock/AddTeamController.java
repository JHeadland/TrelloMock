package com.example.trellomock;

import com.example.trellomock.team.Team;
import com.example.trellomock.team.TeamRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddTeamController implements DialogController{

    @FXML private TextField teamNameField;

    @Autowired
    TeamRepository teamRepository;

    private ScreensConfiguration screens;
    private AdminController adminController;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public AddTeamController(ScreensConfiguration screens) {
        this.screens = screens;
        adminController = screens.adminController();
    }

    public void addNewTeamButton(ActionEvent actionEvent) {
        String teamName = teamNameField.getText();
        if(teamName != null)
            teamRepository.save(new Team(teamName,teamRepository.count()+1));

        adminController.refresh();
        dialog.close();
    }

    public void handleCancelAddTeam(ActionEvent actionEvent) {
        adminController.refresh();
        dialog.close();
    }
}
