package com.gdsc.toplearth_server.domain.entity.matching.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EMatchingStatus {
    NOT_JOINED("팀 미참가"),
    PLOGGING("대결 플로깅 중"),
    DEFAULT("기본 상태"),
    WAITING("매칭 대기중"),
    MATCHED("매칭 완료"),
    FINISHED("매칭 종료");
    private final String status;
}
