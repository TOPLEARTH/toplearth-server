package com.gdsc.toplearth_server.infrastructure.repository.team;

import com.gdsc.toplearth_server.domain.entity.team.Teams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsRepositoryImpl extends JpaRepository<Teams, Long> {
}
