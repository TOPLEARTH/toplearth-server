package com.gdsc.toplearth_server.infrastructure.repository.region;

import com.gdsc.toplearth_server.domain.entity.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepositoryImpl extends JpaRepository<Region, Long> {
}
