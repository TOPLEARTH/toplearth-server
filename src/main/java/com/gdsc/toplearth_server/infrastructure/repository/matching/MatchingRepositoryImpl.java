package com.gdsc.toplearth_server.infrastructure.repository.matching;

import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepositoryImpl extends JpaRepository<Matching, Long> {
    Integer countByTeam(Team team);

    Integer countByTeamAndWinFlagIsTrue(Team team);

    Optional<Matching> findByTeamAndEndedAtIsNull(Team team);

//    @Query("SELECT m FROM Matching m WHERE m.startedAt <= :thresholdTime AND m.endedAt IS NULL")
//    List<Matching> findOngoingMatchingsOlderThan(@Param("thresholdTime") LocalDateTime thresholdTime);
}
