package com.gdsc.toplearth_server.application.dto.mission;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record QuestInfoResponseDto(
        List<QuestDetailResponseDto> dailyQuest,  // 일일 퀘스트 정보
        List<QuestDetailResponseDto> monthlyQuest  // 월간 퀘스트 정보
) {
    public static QuestInfoResponseDto fromEntityList(List<QuestDetailResponseDto> dailyQuest, List<QuestDetailResponseDto> monthlyQuest) {
        return QuestInfoResponseDto.builder()
                .dailyQuest(dailyQuest)
                .monthlyQuest(monthlyQuest)
                .build();
    }
}
