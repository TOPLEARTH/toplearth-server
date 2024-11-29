package com.gdsc.toplearth_server.infrastructure.repository.matching;

import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchingRepositoryImpl extends JpaRepository<Matching, Long> {
    Integer countByTeam(Team team);

    Integer countByTeamAndWinFlagIsTrue(Team team);

    Optional<Matching> findByTeamAndEndedAtIsNull(Team team);

//    @Query("SELECT m FROM Matching m WHERE m.startedAt <= :thresholdTime AND m.endedAt IS NULL")
//    List<Matching> findOngoingMatchingsOlderThan(@Param("thresholdTime") LocalDateTime thresholdTime);

    @Query("SELECT m "
            + "FROM Matching m "
            + "WHERE m.team = :team "
            + "ORDER BY m.startedAt DESC "
            + "LIMIT 1")
    Optional<Matching> findFirstByTeamOrderByStartedAtDesc(@Param("team") Team team);

    @Query("SELECT m "
            + "FROM Matching m "
            + "WHERE m.team = :team And m.opponentTeam = :team1 "
            + "ORDER BY m.startedAt DESC "
            + "LIMIT 1")
    Optional<Matching> findFirstByTeamOrderByStartedAtDesc(@Param("team") Team team, @Param("team1") Team team1);
}
