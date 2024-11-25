package com.gdsc.toplearth_server.infrastructure.repository.region;

import com.gdsc.toplearth_server.application.dto.region.RegionRankProjection;
import com.gdsc.toplearth_server.domain.entity.region.Region;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegionRepositoryImpl extends JpaRepository<Region, Long> {

    @Query("SELECT r.id id, r.name name,  r.totalScore totalScore, RANK() OVER (ORDER BY r.totalScore DESC) as rank FROM Region r")
    List<RegionRankProjection> findAllWithRank();
}
