package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.dto.matching.MatchingStatusResponseDto;
import com.gdsc.toplearth_server.application.service.matching.MatchingService;
import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.domain.entity.matching.type.EMatchingStatus;
import com.gdsc.toplearth_server.infrastructure.message.TeamInfoMessage;
import com.gdsc.toplearth_server.presentation.request.matching.VSFinishRequestDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    // 랜덤 매칭 요청
    @PostMapping("/random")
    public CommonResponseDto<?> randomMatching(
            @UserId UUID userId,
            @RequestBody TeamInfoMessage teamInfoMessage
    ) {
        matchingService.addRandomMatchingRequest(userId, teamInfoMessage);
        return CommonResponseDto.ok(MatchingStatusResponseDto.of(EMatchingStatus.WAITING));
    }

    // 지정 매칭 요청
    @PostMapping("/{opponentTeamId}")
    public CommonResponseDto<?> matchingByTeam(
            @UserId UUID userId,
            @PathVariable Long opponentTeamId,
            @RequestBody TeamInfoMessage teamInfoMessage
    ) {
        matchingService.requestTeamMatching(userId, opponentTeamId, teamInfoMessage);
        return CommonResponseDto.ok(MatchingStatusResponseDto.of(EMatchingStatus.DEFAULT));
    }

    // 지정 매칭 수락
     @PostMapping("/{opponentTeamId}/accept")
     public CommonResponseDto<?> acceptMatching(
             @UserId UUID userId,
             @PathVariable Long opponentTeamId,
             @RequestBody TeamInfoMessage teamInfoMessage
     ) {
         matchingService.addTeamMatchingRequest(userId, opponentTeamId, teamInfoMessage);
         return CommonResponseDto.ok(MatchingStatusResponseDto.of(EMatchingStatus.WAITING));
     }

    // 지정 매칭 거절
    @DeleteMapping("/{opponentTeamId}/reject")
    public CommonResponseDto<?> rejectMatching(
            @UserId UUID userId,
            @PathVariable Long opponentTeamId,
            @RequestBody TeamInfoMessage teamInfoMessage
    ) {
        matchingService.rejectTeamMatching(userId, opponentTeamId, teamInfoMessage);
        return CommonResponseDto.ok(MatchingStatusResponseDto.of(EMatchingStatus.DEFAULT));
    }

    // VS 화면 상태 값 반환
    @GetMapping("/status")
    public CommonResponseDto<?> getMatchingStatus(
            @UserId UUID userId
    ) {
        log.info("Controller getMatchingStatus");
        return CommonResponseDto.ok(matchingService.getMatchingStatus(userId));
    }

    // 매칭 종료
    @PatchMapping("/{matchingId}/end")
    public CommonResponseDto<?> finishVS(
            @PathVariable Long matchingId,
            @RequestBody VSFinishRequestDto vsFinishRequestDto
    ) {
        matchingService.finishVS(matchingId, vsFinishRequestDto);
        return CommonResponseDto.ok(MatchingStatusResponseDto.of(EMatchingStatus.FINISHED));
    }

    //내팀 상대팀 최근 플로깅 조회
    @GetMapping("/plogging")
    public CommonResponseDto<?> recentPlogging(
            @UserId UUID userId
    ) {
        return CommonResponseDto.ok(matchingService.recentPlogging(userId));
    }

}
