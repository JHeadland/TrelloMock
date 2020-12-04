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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class TeamSettingsController implements DialogController, Initializable {

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public TeamSettingsController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML private Text teamHeader;
    @FXML private ComboBox<String> teamComboBox;
    @FXML private TextField teamNameField;
    @FXML private Text deleteWarning;

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;

    private Team team;
    private Member member;
    private boolean warningFlag = false;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        member = memberRepository.findByLogged(true);
        refresh();
    }

    public void refresh() {
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

        team = teamRepository.findByteamName(teamComboBox.getValue());
        teamHeader.setText(team.getTeamName());
    }

    @FXML
    public void clickCancelButton(ActionEvent actionEvent) throws IOException {
        dialog.close();
        screens.boardDialog().show();
    }

    @FXML
    public void clickChangeButton(ActionEvent actionEvent) {
        String teamName = String.valueOf(teamNameField.getText());

        if (!(teamNameField.getText() == null || teamNameField.getText().trim().isEmpty()))
            team.setTeamName(teamName);

        teamRepository.save(team);

        teamNameField.setText("");

        dialog.close();
        screens.boardDialog().show();

        refresh();

        // Test modification
        for (Team Team : teamRepository.findAll())
            System.out.println(Team.getTeamName());
    }

    @FXML
    public void clickLogoutButton(ActionEvent actionEvent) {
        member.setLogged(false);

        teamNameField.setText("");

        dialog.close();
        screens.loginDialog().show();
        screens.adminController().refresh();
    }

}