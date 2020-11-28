package com.example.trellomock.team;

import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team,Long> {

    Team findById(long id);
    void deleteById(long id);
    Team findByteamName(String teamName);
}
