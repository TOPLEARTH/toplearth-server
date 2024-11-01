package com.gdsc.toplearth_server.domain.entity.plogging.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ELabel {
    GENERAL("GENERAL"),        // 일반 쓰레기
    RECYCLABLE("RECYCLABLE"),     // 재활용
    FOOD("FOOD"),           // 음식물
    PLASTIC("PLASTIC"),        // 플라스틱
    PAPER("PAPER"),          // 종이
    GLASS("GLASS"),          // 유리
    METAL("METAL"),          // 금속
    ETC("ETC");

    private final String label;
}
