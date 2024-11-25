package com.gdsc.toplearth_server.application.dto.bootstrap;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record LegacyInfoResponseDto(
        Long totalUserCnt, // 총 유저 수
        Long totalTrashCnt, // 총 쓰레기 수
        TrashInfoResponseDto trashInfo // 함께 주운 쓰레기 정보
) {
    public static LegacyInfoResponseDto of(Long totalUserCnt, Long totalTrashCnt,
                                           TrashInfoResponseDto trashInfo) {
        return LegacyInfoResponseDto.builder()
                .totalUserCnt(totalUserCnt)
                .totalTrashCnt(totalTrashCnt)
                .trashInfo(trashInfo)
                .build();
    }
}
