package com.example.trellomock.team;

import com.example.trellomock.member.Member;
import com.example.trellomock.task.Task;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;

    @Column(unique = true)

    @OneToMany(mappedBy = "team", orphanRemoval = true, fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)

    private Set<Member> members;

    public Team() {
    }

    public Team(String teamName, Long id) {
        this.teamName = teamName;
        this.id = id;
    }

    public void dismissMember(Member m) {
        this.members.remove(m);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStoryPoints() {
        return members.stream().mapToInt(Member::getStoryPoints).sum();
    }
}