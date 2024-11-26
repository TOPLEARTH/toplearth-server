package com.gdsc.toplearth_server.domain.entity.region;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long totalScore;

    //------------------------------------------------

    @OneToMany(mappedBy = "region", cascade = CascadeType.MERGE)
    private List<Plogging> ploggings;

    //------------------------------------------------

    public void updateTotalScore(Double distance) {
        Double score = distance * 100;
        this.totalScore += score.longValue();
    }
}
