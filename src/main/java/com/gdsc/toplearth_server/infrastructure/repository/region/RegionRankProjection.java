package com.gdsc.toplearth_server.infrastructure.repository.region;

public interface RegionRankProjection {
    Long getId();

    String getName();

    Long getTotalScore();

    Integer getRank();
}
