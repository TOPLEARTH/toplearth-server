package com.gdsc.toplearth_server.domain.entity.matching;

import static jakarta.persistence.FetchType.LAZY;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.team.Teams;
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

    @Column(nullable = false)
    private Long competitionScore;

    @Column(nullable = false)
    private Integer totalPickUpCnt;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @Column
    private Boolean winFlag;

    @Column(nullable = false)
    private Boolean competitionStatus;

    //--------------------------------------

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Teams team;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "opponent_id", nullable = false)
    private Teams opponentTeam;

    //--------------------------------------

    @OneToMany(mappedBy = "matching", cascade = CascadeType.MERGE)
    private List<Plogging> ploggings;

    @Builder
    public Matching(String competitionScore, Integer totalPickUpCnt, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.competitionScore = 0L;
        this.totalPickUpCnt = 0;
        this.startedAt = LocalDateTime.now();
        this.endedAt = null;
        this.winFlag = null;
        this.competitionStatus = false;
    }
}
