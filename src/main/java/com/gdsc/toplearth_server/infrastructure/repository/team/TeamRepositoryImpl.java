package com.gdsc.toplearth_server.infrastructure.repository.team;

import com.gdsc.toplearth_server.domain.entity.team.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepositoryImpl extends JpaRepository<Team, Long> {

    List<Team> findByNameContaining(String name);

    Boolean existsByName(String name);

}
