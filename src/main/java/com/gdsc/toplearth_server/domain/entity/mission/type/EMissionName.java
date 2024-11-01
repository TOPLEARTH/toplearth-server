package com.gdsc.toplearth_server.domain.entity.mission.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EMissionName {
    FLOGGING("FLOGGING"),
    PICKUP("PICKUP"),
    LABELING("LABELING");

    private final String missionName;

}
