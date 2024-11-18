package com.gdsc.toplearth_server.infrastructure.repository.mission;

import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepositoryImpl extends JpaRepository<Mission, Long> {
}
