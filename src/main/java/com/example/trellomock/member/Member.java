package com.example.trellomock.member;

import com.example.trellomock.team.Team;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "members")
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long memberID;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean adminPrivileges;
    private boolean logged = false;
    private int storyPoints;

    private ArrayList<Long> tasks;



    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;


    public Member() {}

    public Member(String firstName, String lastName, String email, String password, Team team) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.team = team;
        this.adminPrivileges = false;
        this.tasks = new ArrayList<Long>();
        this.storyPoints = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "User[memberID=%d, firstName='%s', lastName='%s', email='%s', password='%s team='%s' ",
                memberID, firstName, lastName, email, password, team.getTeamName());
    }

    public void assignTask(Long taskID) {
        tasks.add(taskID);
    }

    public Long getMemberID() {
        return memberID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean getAdminPrivileges() {
        return adminPrivileges;
    }

    public void setAdminPrivileges(boolean ap) { this.adminPrivileges = ap; }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) { this.team = team; }

    public Long getTeamID() { return team.getId(); }

    public ArrayList<Long> getAssignedTasks() {
        return tasks;
    }

    public boolean getLogged() { return logged; }

    public void setLogged(boolean logged) { this.logged = logged; }

    public int getStoryPoints() { return this.storyPoints; }

    public void addStoryPoints(int sp) { this.storyPoints += sp; }
}
