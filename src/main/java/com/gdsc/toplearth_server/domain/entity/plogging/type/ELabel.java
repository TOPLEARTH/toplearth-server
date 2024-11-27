package com.gdsc.toplearth_server.domain.entity.plogging.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ELabel {
    PLASTIC("플라스틱"),
    FOOD_WASTE("음식물"),
    GLASS("유리병"),
    CIGARETTE("담배꽁초"),
    PAPER("종이류"),
    GENERAL("일회용 용기"),
    CAN("캔"),
    PLASTIC_BAG("비닐봉지"),
    OTHERS("기타"),
    UNKNOWN("미분류");

    private final String label;
}
