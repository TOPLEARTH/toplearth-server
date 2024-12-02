package com.gdsc.toplearth_server.domain.entity.mission.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EMissionName {
    PLOGGING("PLOGGING"),
    PICKUP("PICKUP"),
    LABELING("LABELING");

    private final String missionName;

}
