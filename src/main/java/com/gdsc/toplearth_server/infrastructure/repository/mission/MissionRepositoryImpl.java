package com.gdsc.toplearth_server.infrastructure.repository.mission;

import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import com.gdsc.toplearth_server.domain.entity.mission.type.EMissionType;
import com.gdsc.toplearth_server.domain.entity.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepositoryImpl extends JpaRepository<Mission, Long> {
    List<Mission> findByUserAndMissionType(User user, EMissionType eMissionType);
}
