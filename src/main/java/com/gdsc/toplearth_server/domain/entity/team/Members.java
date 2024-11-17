package com.gdsc.toplearth_server.domain.entity.team;

import static jakarta.persistence.FetchType.LAZY;

import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import com.gdsc.toplearth_server.domain.entity.user.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ETeamRole eTeamRole;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    //-------------------------------------------

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    //-------------------------------------------

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Teams team;

    //-------------------------------------------

    @Builder
    public Members(ETeamRole eTeamRole, Users user, Teams team) {
        this.eTeamRole = eTeamRole;
        this.joinedAt = LocalDateTime.now();
        this.team = team;
        this.user = user;
    }

    public static Members toMemberEntity(ETeamRole eTeamRole, Users user, Teams team) {
        return Members.builder()
                .eTeamRole(eTeamRole)
                .user(user)
                .team(team)
                .build();
    }

}
