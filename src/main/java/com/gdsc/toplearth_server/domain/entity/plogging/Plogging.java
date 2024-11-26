package com.gdsc.toplearth_server.domain.entity.plogging;

import static jakarta.persistence.FetchType.LAZY;

import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.region.Region;
import com.gdsc.toplearth_server.domain.entity.report.Report;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.user.User;
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
@Table(name = "plogging")
public class Plogging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Integer pickUpCnt;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @Column(nullable = false)
    private Long duration;

    @Column(length = 2048)
    private String image;

    @Column(nullable = false)
    private Integer burnedCalories;

    //-------------------------------------------

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "matching_id")
    private Matching matching;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    //------------------------------------------------

    @OneToMany(mappedBy = "plogging", cascade = CascadeType.MERGE)
    private List<PloggingImage> ploggingImages;

    @OneToMany(mappedBy = "plogging", cascade = CascadeType.MERGE)
    private List<Report> reports;

    //-------------------------------------------

    @Builder(access = AccessLevel.PRIVATE)
    public Plogging(User user, Region region, Team team, Matching matching) {
        this.distance = 0.0;
        this.pickUpCnt = 0;
        this.startedAt = LocalDateTime.now();
        this.endedAt = null;
        this.duration = 0L;
        this.image = null;
        this.burnedCalories = 0;
        this.team = team;
        this.matching = matching;
        this.user = user;
        this.region = region;
    }

    public static Plogging createUserPlogging(
            User user, Region region,
            Team team, Matching matching
    ) {
        return Plogging.builder()
                .user(user)
                .region(region)
                .team(team)
                .matching(matching)
                .build();
    }

    public void updatePlogging(
            Double distance, Integer pickUpCnt,
            Long duration, String ploggingInfoImage,
            Integer burnedCalories
    ) {
        this.distance = distance;
        this.pickUpCnt = pickUpCnt;
        this.duration = duration;
        this.image = ploggingInfoImage;
        this.burnedCalories = burnedCalories;
        this.endedAt = LocalDateTime.now();
    }

}
