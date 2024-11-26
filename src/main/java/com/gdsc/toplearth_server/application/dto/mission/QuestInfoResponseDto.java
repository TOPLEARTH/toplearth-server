package com.gdsc.toplearth_server.application.dto.mission;

import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record QuestInfoResponseDto(
        Map<String, List<QuestDetailResponseDto>> dailyQuest  // 일일 퀘스트 정보
) {
    public static QuestInfoResponseDto fromEntityList(Map<String, List<QuestDetailResponseDto>> dailyQuest) {
        return QuestInfoResponseDto.builder()
                .dailyQuest(dailyQuest)
                .build();
    }
}
