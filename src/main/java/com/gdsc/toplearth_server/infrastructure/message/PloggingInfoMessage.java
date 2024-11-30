package com.gdsc.toplearth_server.infrastructure.message;

public record PloggingInfoMessage(
        Long ploggingId, // 플로깅 ID
        Double distance, // 플로깅 거리
        Integer trashCnt, // 쓰레기 개수
        boolean isActive // 플로깅 활성 상태
) {
}
