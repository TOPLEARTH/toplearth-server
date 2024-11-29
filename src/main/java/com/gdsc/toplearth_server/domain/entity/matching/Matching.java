package com.gdsc.toplearth_server.domain.entity.matching;

import static jakarta.persistence.FetchType.LAZY;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "matching")
public class Matching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "competition_score", nullable = false)
    private Long competitionScore;

    @Column(name = "total_pick_up_cnt", nullable = false)
    private Integer totalPickUpCnt;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "win_flag")
    private Boolean winFlag;

    @Column(name = "competition_status", nullable = false)
    private Boolean competitionStatus;

    //--------------------------------------

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "opponent_id", nullable = false)
    private Team opponentTeam;

    //--------------------------------------

    @OneToMany(mappedBy = "matching", cascade = CascadeType.MERGE)
    private List<Plogging> ploggings;

    @Builder(access = AccessLevel.PRIVATE)
    public Matching(Team team, Team opponentTeam) {
        this.competitionScore = 0L;
        this.totalPickUpCnt = 0;
        this.startedAt = LocalDateTime.now();
        this.endedAt = null;
        this.winFlag = null;
        this.competitionStatus = true;
        this.team = team;
        this.opponentTeam = opponentTeam;
    }

    public static Matching fromTeamEntities(Team team, Team opponentTeam) {
        return Matching.builder()
                .team(team)
                .opponentTeam(opponentTeam)
                .build();
    }

    public void finishVS(
            Boolean winFlag, Long competitionScore, Integer totalPickUpCnt
    ) {
        this.endedAt = LocalDateTime.now();
        this.competitionStatus = false;
        this.winFlag = winFlag;
        this.competitionScore = competitionScore;
        this.totalPickUpCnt = totalPickUpCnt;
    }
}
