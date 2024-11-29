package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
import lombok.Builder;

@Builder
public record LabelInfo(
        String label,
        Long labelCnt
) {
    public static LabelInfo of(ELabel eLabel, Long labelCnt) {
        return LabelInfo.builder()
                .label(eLabel.name())
                .labelCnt(labelCnt)
                .build();
    }
}
