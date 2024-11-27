package com.gdsc.toplearth_server.domain.entity.plogging;

import static jakarta.persistence.FetchType.LAZY;

import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
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
@Table(name = "plogging_image")
public class PloggingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String image;

    @Column
    @Enumerated(EnumType.STRING)
    private ELabel eLabel;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    //-------------------------------------------

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "plogging_id", nullable = false)
    private Plogging plogging;

    //-------------------------------------------

    @Builder(access = AccessLevel.PRIVATE)
    public PloggingImage(
            Plogging plogging, String image,
            Double latitude, Double longitude
    ) {
        this.plogging = plogging;
        this.image = image;
        this.eLabel = ELabel.UNKNOWN;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now();
    }

    public static PloggingImage createPloggingImage(
            Plogging plogging, String image,
            Double latitude, Double longitude
    ) {
        return PloggingImage.builder()
                .plogging(plogging)
                .image(image)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public void updateImageLabel(ELabel eLabel) {
        this.eLabel = eLabel;
    }
}
