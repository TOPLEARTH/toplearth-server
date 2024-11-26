package com.gdsc.toplearth_server.application.dto.mission;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record QuestDetailResponseDto(
//        Long questId, // 퀘스트 ID
//        String title,   // 퀘스트 제목
//        Integer target,    // 퀘스트 목표
//        Integer questCredit, // 퀘스트 크레딧
//        Integer currentProgress,  // 현재 진행 상황
//        Boolean isCompleted,    // 퀘스트 완료 여부
//        Integer progressRate, // 퀘스트 진행률
//        String createdAt,    // 퀘스트 생성일
//        String completedAt  //퀘스트 완료일
        Integer targetKmName,
        Integer targetPickNumber,
        Integer targetLabelNumber,
        Integer myKmNumber,
        Integer myPickNumber,
        Integer myLabelNumber,
        String createdAt
) {
    public static QuestDetailResponseDto fromMissionEntity(Integer targetKmName, Integer targetPickNumber,
                                                           Integer targetLabelNumber, Integer myKmNumber,
                                                           Integer myPickNumber, Integer myLabelNumber,
                                                           String createdAt) {
        return QuestDetailResponseDto.builder()
                .targetKmName(targetKmName)
                .targetPickNumber(targetPickNumber)
                .targetLabelNumber(targetLabelNumber)
                .myKmNumber(myKmNumber)
                .myPickNumber(myPickNumber)
                .myLabelNumber(myLabelNumber)
                .createdAt(createdAt)
                .build();


    }
}
