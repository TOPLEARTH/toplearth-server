package com.gdsc.toplearth_server.domain.entity.mission.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EMissionType {
    DAILY("DAILY"),
    MONTH("MONTH");

    private final String missionType;
}
