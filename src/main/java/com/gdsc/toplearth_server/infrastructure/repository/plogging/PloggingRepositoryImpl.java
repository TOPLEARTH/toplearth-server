package com.gdsc.toplearth_server.infrastructure.repository.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PloggingRepositoryImpl extends JpaRepository<Plogging, Long> {

    @Query("SELECT e FROM Plogging e WHERE YEAR(e.startedAt) = :year and e.team = :team")
    List<Plogging> findByYearAndTeam(@Param("year") int year, @Param("team") Team team);

    List<Plogging> findByUser(User user);

    @Query("SELECT p.matching.team FROM Plogging p WHERE p.user.id = :userId")
    Optional<Team> findTeamByUserId(@Param("userId") UUID userId);

    @Query("SELECT p.matching.opponentTeam FROM Plogging p WHERE p.user.id = :userId")
    Optional<Team> findOpponentTeamByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(*) ploggingMonthlyCount, COALESCE(SUM(p.duration), 0) ploggingMonthlyDuration, COALESCE(SUM(p.burnedCalories), 0) burnedCalories "
            + "FROM Plogging p "
            + "WHERE p.user = :user "
            + "AND DATE_FORMAT(p.startedAt, '%Y-%m') = :startedAt")
    PloggingProjection findByUserAndCreatedAt(@Param("user") User user, @Param("startedAt") String startedAt);

    Optional<Plogging> findByUserAndId(User user, Long ploggingId);

    @Query("SELECT COALESCE(SUM(p.duration), 0) duration, COALESCE(SUM(p.pickUpCnt), 0) trashCount, FUNCTION('DATE_FORMAT', p.startedAt, '%Y-%m') startAt "
            +
            "FROM Plogging p " +
            "GROUP BY FUNCTION('DATE_FORMAT', p.startedAt, '%Y-%m') " +
            "ORDER BY p.startedAt")
    List<AdminPloggingProjection> ploggingMonthly();
}
