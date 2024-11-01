package com.gdsc.toplearth_server.infrastructure.repository.mission;

import com.gdsc.toplearth_server.domain.entity.mission.Missions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionsRepositoryImpl extends JpaRepository<Missions, Long> {
}
