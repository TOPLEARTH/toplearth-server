package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.dto.matching.MatchingStatusResponseDto;
import com.gdsc.toplearth_server.application.service.matching.MatchingService;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.domain.entity.matching.type.EMatchingStatus;
import com.gdsc.toplearth_server.infrastructure.message.TeamInfoMessage;
import com.gdsc.toplearth_server.presentation.request.matching.MatchingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    // 랜덤 매칭 요청
    @PostMapping("/random")
    public CommonResponseDto<?> randomMatching(@RequestBody TeamInfoMessage teamInfoMessage) {
        matchingService.addRandomMatchingRequest(teamInfoMessage);
        return CommonResponseDto.ok(MatchingStatusResponseDto.of(EMatchingStatus.WAITING));
    }

    // 지정 매칭 요청
    @PostMapping("/{opponentTeamId}")
    public CommonResponseDto<?> matchingByTeam(
            @PathVariable Long opponentTeamId,
            @RequestBody MatchingRequestDto matchingRequestDto
    ) {
        matchingService.addTeamMatchingRequest(opponentTeamId, matchingRequestDto);
        return CommonResponseDto.ok(MatchingStatusResponseDto.of(EMatchingStatus.WAITING));
    }
}
