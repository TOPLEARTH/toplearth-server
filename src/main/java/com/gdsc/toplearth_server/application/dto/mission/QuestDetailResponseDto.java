package com.gdsc.toplearth_server.application.dto.mission;

import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record QuestDetailResponseDto(
        String questId, // 퀘스트 ID
        String title,   // 퀘스트 제목
        String target,    // 퀘스트 목표
        String currentProgress,  // 현재 진행 상황
        Boolean isCompleted,    // 퀘스트 완료 여부
        String progressRate, // 퀘스트 진행률
        String createdAt,    // 퀘스트 생성일
        String completedAt   // 퀘스트 완료일
) {
    public static QuestDetailResponseDto fromMissionEntity(Mission questDetail) {
        return QuestDetailResponseDto.builder()
                .questId(questDetail.getId().toString())
                .title(questDetail.getMissionName().toString())
                .target(questDetail.getTarget().toString())
                .currentProgress(questDetail.getCurrent().toString())
                .isCompleted(questDetail.getIsCompleted())
                .progressRate(questDetail.getProgressRate().toString())
                .createdAt(questDetail.getCreatedAt().toString())
                .completedAt(questDetail.getCompletedAt().toString())
                .build();
    }
}
