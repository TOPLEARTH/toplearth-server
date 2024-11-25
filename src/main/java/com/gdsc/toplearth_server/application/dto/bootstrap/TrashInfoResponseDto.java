package com.gdsc.toplearth_server.application.dto.bootstrap;

import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record TrashInfoResponseDto(
        Long plastic, // 플라스틱
        Long foodWaste, // 음식물
        Long glassBottle, // 유리병
        Long cigaretteButt, // 담배꽁초
        Long paper, // 종이류
        Long disposableContainer, // 일회용 용기
        Long can, // 캔
        Long plasticBag, // 비닐봉지
        Long others // 기타
) {
    public static TrashInfoResponseDto fromTrashCountMap(Map<ELabel, Long> trashCountMap) {
        return TrashInfoResponseDto.builder()
                .plastic(trashCountMap.getOrDefault(ELabel.PLASTIC, 0L))
                .foodWaste(trashCountMap.getOrDefault(ELabel.FOOD_WASTE, 0L))
                .glassBottle(trashCountMap.getOrDefault(ELabel.GLASS, 0L))
                .cigaretteButt(trashCountMap.getOrDefault(ELabel.CIGARETTE, 0L))
                .paper(trashCountMap.getOrDefault(ELabel.PAPER, 0L))
                .disposableContainer(trashCountMap.getOrDefault(ELabel.GENERAL, 0L))
                .can(trashCountMap.getOrDefault(ELabel.CAN, 0L))
                .plasticBag(trashCountMap.getOrDefault(ELabel.PLASTIC_BAG, 0L))
                .others(trashCountMap.getOrDefault(ELabel.OTHERS, 0L))
                .build();
    }
}
