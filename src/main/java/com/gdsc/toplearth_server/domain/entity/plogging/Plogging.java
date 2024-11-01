package com.gdsc.toplearth_server.domain.entity.plogging;

import static jakarta.persistence.FetchType.LAZY;

import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.region.Region;
import com.gdsc.toplearth_server.domain.entity.report.Reports;
import com.gdsc.toplearth_server.domain.entity.team.Teams;
import com.gdsc.toplearth_server.domain.entity.user.Users;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "plogging")
public class Plogging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal distance;

    @Column
    private Integer pickUpCnt;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false, length = 2048)
    private String image;

    //-------------------------------------------

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Teams teams;

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
    private List<Reports> reports;
}
