package com.gdsc.toplearth_server.application.dto.mission;

import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record QuestDetailResponseDto(
        Long questId, // 퀘스트 ID
        String title,   // 퀘스트 제목
        Integer target,    // 퀘스트 목표
        Integer questCredit, // 퀘스트 크레딧
        Integer currentProgress,  // 현재 진행 상황
        Boolean isCompleted,    // 퀘스트 완료 여부
        Integer progressRate, // 퀘스트 진행률
        String createdAt,    // 퀘스트 생성일
        String completedAt   // 퀘스트 완료일
) {
    public static QuestDetailResponseDto fromMissionEntity(Mission questDetail) {
        return QuestDetailResponseDto.builder()
                .questId(questDetail.getId())
                .title(questDetail.getMissionName() == null ? null : questDetail.getMissionName().toString())
                .target(questDetail.getTarget())
                .questCredit(questDetail.getCredit())
                .currentProgress(questDetail.getCurrent())
                .isCompleted(questDetail.getIsCompleted() == null ? null : questDetail.getIsCompleted())
                .progressRate(questDetail.getProgressRate())
                .createdAt(questDetail.getCreatedAt() == null ? null : questDetail.getCreatedAt().toString())
                .completedAt(questDetail.getCompletedAt() == null ? null : questDetail.getCompletedAt().toString())
                .build();
    }
}
