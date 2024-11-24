package com.gdsc.toplearth_server.application.dto.bootstrap;

import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record TrashInfoResponseDto(
        String plastic, // 플라스틱
        String foodWaste, // 음식물
        String glassBottle, // 유리병
        String cigaretteButt, // 담배꽁초
        String paper, // 종이류
        String disposableContainer, // 일회용 용기
        String can, // 캔
        String plasticBag, // 비닐봉지
        String others // 기타
) {
    public static TrashInfoResponseDto fromTrashCountMap(Map<ELabel, Long> trashCountMap) {
        return TrashInfoResponseDto.builder()
                .plastic(String.valueOf(trashCountMap.getOrDefault(ELabel.PLASTIC, 0L)))
                .foodWaste(String.valueOf(trashCountMap.getOrDefault(ELabel.FOOD_WASTE, 0L)))
                .glassBottle(String.valueOf(trashCountMap.getOrDefault(ELabel.GLASS, 0L)))
                .cigaretteButt(String.valueOf(trashCountMap.getOrDefault(ELabel.CIGARETTE, 0L)))
                .paper(String.valueOf(trashCountMap.getOrDefault(ELabel.PAPER, 0L)))
                .disposableContainer(String.valueOf(trashCountMap.getOrDefault(ELabel.GENERAL, 0L)))
                .can(String.valueOf(trashCountMap.getOrDefault(ELabel.CAN, 0L)))
                .plasticBag(String.valueOf(trashCountMap.getOrDefault(ELabel.PLASTIC_BAG, 0L)))
                .others(String.valueOf(trashCountMap.getOrDefault(ELabel.OTHERS, 0L)))
                .build();
    }
}
