package com.gdsc.toplearth_server.domain.entity.mission;

import static jakarta.persistence.FetchType.LAZY;

import com.gdsc.toplearth_server.domain.entity.mission.type.EMissionName;
import com.gdsc.toplearth_server.domain.entity.mission.type.EMissionType;
import com.gdsc.toplearth_server.domain.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private EMissionName missionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EMissionType missionType;

    @Column(name = "current", nullable = false)
    private Integer current;

    @Column(name = "target", nullable = false)
    private Integer target;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Column(name = "credit", nullable = false)
    private Integer credit;

    //-------------------------------------------

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //-------------------------------------------

    @Builder(access = AccessLevel.PRIVATE)
    public Mission(EMissionName missionName, EMissionType missionType, Integer target, Integer credit, User user) {
        this.missionName = missionName;
        this.missionType = missionType;
        this.current = 0;
        this.target = target;
        this.createdAt = LocalDateTime.now();
        this.isCompleted = false;
        this.credit = credit;
        this.user = user;
    }

    public static Mission createMission(
            EMissionName missionName, EMissionType missionType, Integer target, Integer credit, User user
    ) {
        return Mission.builder()
                .missionName(missionName)
                .missionType(missionType)
                .target(target)
                .credit(credit)
                .user(user)
                .build();
    }

    //-------------------------------------------

    public Integer getProgressRate() {
        return (int) ((double) current / target * 100);
    }

    public void updatePloggingMission(Double distance) {
        int current = this.current + (int) Math.round(distance);
        this.current += current;
        this.isCompleted = current >= target;
        this.completedAt = isCompleted ? LocalDateTime.now() : null;
    }

    public void updateMission(Integer cnt) {
        int current = this.current + cnt;
        this.current += current;
        this.isCompleted = current >= target;
        this.completedAt = isCompleted ? LocalDateTime.now() : null;

    }
}
