package com.gdsc.toplearth_server.application.dto.region;

public interface RegionRankProjection {
    Long getId();

    String getName();

    Long getTotalScore();

    Integer getRank();
}
