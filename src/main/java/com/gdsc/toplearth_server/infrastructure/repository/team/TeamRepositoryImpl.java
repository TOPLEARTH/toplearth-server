package com.gdsc.toplearth_server.infrastructure.repository.team;

import com.gdsc.toplearth_server.domain.entity.team.Team;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepositoryImpl extends JpaRepository<Team, Long> {

    List<Team> findByNameContaining(String name);

    Boolean existsByName(String name);

    @Query("SELECT t FROM Team t WHERE (:name IS NULL OR t.name LIKE CONCAT('%', :name, '%'))")
    Page<Team> searchItemList(@Param("name") String name, Pageable pageable);

}
