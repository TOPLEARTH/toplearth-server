package com.gdsc.toplearth_server.infrastructure.repository.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PloggingRepositoryImpl extends JpaRepository<Plogging, Long> {

    @Query("SELECT e FROM Plogging e WHERE YEAR(e.startedAt) = :year and e.team = :team")
    List<Plogging> findByYearAndTeam(@Param("year") int year, @Param("team") Team team);
}
