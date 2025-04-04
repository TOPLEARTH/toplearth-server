package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.domain.entity.report.Report;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record ReadReportResponseDto(
        Long reportId,
        Long ploggingId,
        String nickname,
        String reportDate,
        Boolean isExecuted,
        Integer reportsCnt,
        Double distance,
        Integer pickUpCnt
) {
    public static ReadReportResponseDto of(Report report) {
        return ReadReportResponseDto.builder()
                .reportId(report.getId())
                .ploggingId(report.getPlogging().getId())
                .nickname(report.getPlogging().getUser().getNickname())
                .reportDate(report.getCreatedAt().format(DateTimeFormatter.ISO_DATE))
                .isExecuted(report.getIsExecuted())
                .reportsCnt(report.getUser().getReportsCnt())
                .distance(report.getPlogging().getDistance())
                .pickUpCnt(report.getPlogging().getPickUpCnt())
                .build();
    }
}
