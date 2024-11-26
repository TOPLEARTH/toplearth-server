package com.gdsc.toplearth_server.presentation.request.plogging;

public record UpdatePloggingRequestDto(
    Double distance,    // 플로깅 거리
    Long duration,  // 플로깅 시간
    Integer pickUpCnt,   // 쓰레기 주운 횟수
    Integer burnedCalories  // 소모 칼로리
) {
}
