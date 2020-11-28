package com.example.trellomock.member;

import java.util.List;


import com.example.trellomock.team.Team;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

    List<Member> findByLastName(String lastName);
    List<Member> findByTeam(Team team);
    Member findByEmail(String email);
    Member findById(long id);
    Member findByLogged(boolean Logged);
}