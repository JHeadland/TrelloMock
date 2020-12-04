package com.example.trellomock;

import com.example.trellomock.member.Member;
import com.example.trellomock.member.MemberRepository;
import com.example.trellomock.task.Task;
import com.example.trellomock.task.TaskRepository;
import com.example.trellomock.team.Team;
import com.example.trellomock.team.TeamRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements DialogController, Initializable {

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public AdminController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML private ComboBox<String> teamComboBox;
    @FXML private Text teamSP;
    @FXML private Text memberSP;
    @FXML private ListView memberList;

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TaskRepository taskRepository;

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

        displayMembers();
    }

    public void displayMembers() {
        memberList.getItems().clear();
        team = teamRepository.findByteamName(teamComboBox.getValue());

        Object[] members = team.getMembers().toArray();

        // Bubble sort; replace with Comparators
        Member mem1;
        Member mem2;
        for (int i = 0; i < members.length-1; i++)
            for (int j = 0; j < members.length-i-1; j++) {
                mem1 = (Member) members[j];
                mem2 = (Member) members[j+1];
                if (mem1.getMemberID() > mem2.getMemberID()) {
                    Object temp = members[j];
                    members[j] = members[j + 1];
                    members[j + 1] = temp;
                }
            }

        Member member;
        String mspStr = "";
        for (int i = 0; i < members.length; i++) {
            member = (Member) members[i];
            memberList.getItems().add(member.getFirstName() + " " + member.getLastName());
            ArrayList<Long> tasks = member.getAssignedTasks();
            Member finalMember = member;
            tasks.forEach(tid -> {
                if (taskRepository.findById(tid).get().GetState() == 4)
                    finalMember.addStoryPoints(taskRepository.findById(tid).get().GetSPoints());
            });
            mspStr += finalMember.getStoryPoints() + "\n";
        }

        teamSP.setText(String.valueOf(team.getStoryPoints()));
        memberSP.setText(mspStr);
    }

    @FXML
    public void clickBoardButton(ActionEvent actionEvent) {
        dialog.close();
        screens.boardDialog().show();
    }

    @FXML
    public void HandleAddTaskClicked(ActionEvent actionEvent) throws IOException {
        screens.addTeamDialog().show();
    }

    public void clickComboBoxTeam(ActionEvent actionEvent) {
        displayMembers();
    }
}