package com.example.trellomock;

import com.example.trellomock.member.Member;
import com.example.trellomock.member.MemberRepository;
import com.example.trellomock.team.Team;
import com.example.trellomock.team.TeamRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterController implements DialogController, Initializable {

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public RegisterController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML private ComboBox<String> teamComboBox;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> teams = new ArrayList<>();
        for (Team team : teamRepository.findAll())
            teams.add(team.getTeamName());
        teamComboBox.setItems(FXCollections.observableArrayList(teams));
    }

    @FXML
    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        dialog.close();
        screens.loginDialog().show();
    }

    @FXML
    public void clickRegisterButton(ActionEvent actionEvent) {
        String firstName = String.valueOf(firstNameField.getText());
        String lastName = String.valueOf(lastNameField.getText());
        String email = String.valueOf(emailField.getText());
        String password = String.valueOf(passwordField.getText());
        Team team = teamRepository.findByteamName(teamComboBox.getValue());

        Member member = new Member(firstName, lastName, email, password, team);
        member.setLogged(true);
        memberRepository.save(member);

        dialog.close();
        screens.boardDialog().show();

        // Test registration
        for (Member Member : memberRepository.findAll())
            System.out.println(Member.toString());
    }

    public void onEnter(ActionEvent actionEvent) {
        clickRegisterButton(actionEvent);
    }
}