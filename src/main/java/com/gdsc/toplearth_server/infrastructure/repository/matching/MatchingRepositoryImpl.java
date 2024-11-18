package com.gdsc.toplearth_server.infrastructure.repository.matching;

import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepositoryImpl extends JpaRepository<Matching, Long> {
    Integer countByTeam(Team team);

    Integer countByTeamAndWinFlagIsTrue(Team team);
}
