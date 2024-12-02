package com.gdsc.toplearth_server.infrastructure.repository.mission;

import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import com.gdsc.toplearth_server.domain.entity.mission.type.EMissionName;
import com.gdsc.toplearth_server.domain.entity.mission.type.EMissionType;
import com.gdsc.toplearth_server.domain.entity.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MissionRepositoryImpl extends JpaRepository<Mission, Long> {
    List<Mission> findByUserAndMissionType(User user, EMissionType eMissionType);

    @Query("SELECT m FROM Mission m WHERE m.user = :user " +
            "AND m.missionName = :missionName " +
            "AND m.isCompleted = :isCompleted " +
            "AND DATE(m.createdAt) = :createdAtDate")
    Optional<Mission> findByUserAndMissionNameAndIsCompletedAndCreatedAtDate(
            @Param("user") User user,
            @Param("missionName") EMissionName missionName,
            @Param("isCompleted") boolean isCompleted,
            @Param("createdAtDate") LocalDate createdAtDate);
}
