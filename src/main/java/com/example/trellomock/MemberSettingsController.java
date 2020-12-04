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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MemberSettingsController implements DialogController, Initializable {

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public MemberSettingsController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML private Text memberHeader;
    @FXML private ComboBox<String> teamComboBox;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Text deleteWarning;

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;

    private Member member;
    private boolean warningFlag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
    }

    public void refresh() {
        warningFlag = false;
        member = memberRepository.findByLogged(true);
        memberHeader.setText(member.getFirstName() + " " + member.getLastName());

        int teamIndex = 0;
        int index = 0;
        List<String> teams = new ArrayList<>();
        for (Team team : teamRepository.findAll()) {
            teams.add(team.getTeamName());
            if (team.getId() == member.getTeamID())
                teamIndex = index;
            index++;
        }
        teamComboBox.setItems(FXCollections.observableArrayList(teams));
        teamComboBox.getSelectionModel().select(teamIndex);
    }

    @FXML
    public void clickCancelButton(ActionEvent actionEvent) throws IOException {
        dialog.close();
        screens.boardDialog().show();
    }

    @FXML
    public void clickChangeButton(ActionEvent actionEvent) {
        String email = String.valueOf(emailField.getText());
        String password = String.valueOf(passwordField.getText());
        Team team = teamRepository.findByteamName(teamComboBox.getValue());

        member.setTeam(team);
        if (!(emailField.getText() == null || emailField.getText().trim().isEmpty()))
            member.setEmail(email);
        if (!(passwordField.getText() == null || passwordField.getText().trim().isEmpty()))
            member.setPassword(password);

        memberRepository.save(member);

        emailField.setText("");
        passwordField.setText("");

        dialog.close();
        screens.boardDialog().show();
        refresh();

        // Test modification
        for (Member Member : memberRepository.findAll())
            System.out.println(Member.toString());
    }

    @FXML
    public void clickLogoutButton(ActionEvent actionEvent) {
        member.setLogged(false);

        emailField.setText("");
        passwordField.setText("");

        dialog.close();
        screens.loginDialog().show();
        refresh();
        screens.adminController().refresh();
    }

    @FXML
    public void clickDeleteButton(ActionEvent actionEvent) {
        if (!warningFlag) {
            deleteWarning.setText("Deletion is irreversible.\nClick delete again to confirm");
            deleteWarning.setFill(Color.RED);
            warningFlag = true;
        }
        else {
            emailField.setText("");
            passwordField.setText("");

            memberRepository.deleteById(member.getMemberID());

            dialog.close();
            screens.loginDialog().show();

            // Test deletion
            for (Member Member : memberRepository.findAll())
                System.out.println(Member.toString());
        }
    }
}