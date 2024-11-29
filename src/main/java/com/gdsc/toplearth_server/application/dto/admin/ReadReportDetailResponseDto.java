package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.application.dto.plogging.PloggingImageDetailResponseDto;
import com.gdsc.toplearth_server.domain.entity.report.Report;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;

@Builder
public record ReadReportDetailResponseDto(
        Long reportId,
        Long ploggingId,
        String nickname,
        String reportDate,
        Boolean isExecuted,
        Integer reportsCnt,
        Double distance,
        Integer pickUpCnt,
        String ploggingImage,
        List<PloggingImageDetailResponseDto> ploggingImageList
) {
    public static ReadReportDetailResponseDto of(Report report,
                                                 List<PloggingImageDetailResponseDto> ploggingImageList) {
        return ReadReportDetailResponseDto.builder()
                .reportId(report.getId())
                .ploggingId(report.getPlogging().getId())
                .nickname(report.getPlogging().getUser().getNickname())
                .reportDate(report.getCreatedAt().format(DateTimeFormatter.ISO_DATE))
                .isExecuted(report.getIsExecuted())
                .reportsCnt(report.getUser().getReportsCnt())
                .distance(report.getPlogging().getDistance())
                .pickUpCnt(report.getPlogging().getPickUpCnt())
                .ploggingImage(report.getPlogging().getImage())
                .ploggingImageList(ploggingImageList)
                .build();
    }
}
