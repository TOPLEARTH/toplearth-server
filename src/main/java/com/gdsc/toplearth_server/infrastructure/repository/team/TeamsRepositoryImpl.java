package com.gdsc.toplearth_server.infrastructure.repository.team;

import com.gdsc.toplearth_server.domain.entity.team.Teams;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsRepositoryImpl extends JpaRepository<Teams, Long> {

    List<Teams> findByNameContaining(String name);

    Boolean existsByName(String name);

}
