package com.gdsc.toplearth_server.domain.entity.matching.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EMatchingStatus {
    WAITING("매칭 대기중"),
    MATCHED("매칭 완료"),
    FINISHED("매칭 종료");

    private final String status;
}
