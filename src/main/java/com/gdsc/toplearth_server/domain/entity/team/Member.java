package com.gdsc.toplearth_server.domain.entity.team;

import static jakarta.persistence.FetchType.LAZY;

import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
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
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ETeamRole teamRole;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    //-------------------------------------------

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //-------------------------------------------

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    //-------------------------------------------

    @Builder
    public Member(ETeamRole eTeamRole, User user, Team team) {
        this.teamRole = eTeamRole;
        this.joinedAt = LocalDateTime.now();
        this.team = team;
        this.user = user;
    }

    public static Member toMemberEntity(ETeamRole eTeamRole, User user, Team team) {
        return Member.builder()
                .eTeamRole(eTeamRole)
                .user(user)
                .team(team)
                .build();
    }

}
