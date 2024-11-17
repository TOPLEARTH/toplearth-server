package com.gdsc.toplearth_server.domain.entity.team;

import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Column
    private LocalDateTime createdAt;

    //------------------------------------------------

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Members> members;

    @OneToMany(mappedBy = "team", cascade = CascadeType.MERGE)
    private List<Plogging> ploggings;

    @OneToMany(mappedBy = "team", cascade = CascadeType.MERGE)
    private List<Matching> matchings;

    //--------------------------------------------------

    @Builder
    public Teams(String name, String code) {
        this.name = name;
        this.code = code;
        this.createdAt = LocalDateTime.now();
    }

    public static Teams toTeamEntity(String name, String code) {
        return Teams.builder()
                .name(name)
                .code(code)
                .build();
    }

    //--------------------------------------------------

    public void updateName(String name) {
        this.name = name;
    }

    public void updateCode(String code) {
        this.code = code;
    }

}
