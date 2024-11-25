package com.gdsc.toplearth_server.presentation.controller.team;

import com.gdsc.toplearth_server.application.service.team.TeamService;
import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.presentation.request.team.CreateTeamRequestDto;
import com.gdsc.toplearth_server.presentation.request.team.UpdateTeamNameRequestDto;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

//    @GetMapping("")
//    public CommonResponseDto<?> teamInfo(
//            @UserId UUID userId
//    ) {
//        return CommonResponseDto.ok(teamService.readTeam(userId));
//    }

    //팀 검색
    @GetMapping("/search")
    public CommonResponseDto<?> searchTeam(
            @RequestParam(value = "text", required = false) String searchTeam
    ) {
        return CommonResponseDto.ok(teamService.searchTeam(searchTeam));
    }

    //팀 이름 변경
    @PatchMapping("/{teamId}/name")
    public CommonResponseDto<?> updateTeamName(
            @PathVariable Long teamId,
            @Valid @RequestBody UpdateTeamNameRequestDto updateTeamNameRequestDto,
            @UserId UUID userId
    ) {
        return CommonResponseDto.ok(teamService.updateTeamName(teamId, updateTeamNameRequestDto, userId));
    }

    //팀 코드 변경
    @PatchMapping("/{teamId}/code")
    public CommonResponseDto<?> updateTeamCode(
            @PathVariable Long teamId,
            @UserId UUID userId
    ) {
        return CommonResponseDto.ok(teamService.updateTeamCode(teamId, userId));
    }

    //팀 팀원 강퇴
    @DeleteMapping("/{teamId}/members/{memberId}")
    public CommonResponseDto<?> deleteTeamMember(
            @PathVariable(name = "teamId", required = false) Long teamId,
            @PathVariable(name = "memberId", required = false) Long memberId,
            @UserId UUID userId
    ) {
        teamService.deleteTeamMember(teamId, memberId, userId);
        return CommonResponseDto.ok(null);
    }

    //팀 나가기
    @DeleteMapping("/{teamId}")
    public CommonResponseDto<?> deleteTeam(
            @PathVariable(name = "teamId", required = false) Long teamId,
            @UserId UUID userId
    ) {
        teamService.deleteTeam(teamId, userId);
        return CommonResponseDto.ok(null);
    }

    //팀생성
    @PostMapping("")
    public CommonResponseDto<?> createTeam(
            @RequestBody @Valid CreateTeamRequestDto createTeamRequestDto,
            @UserId UUID userId
    ) {
        return CommonResponseDto.ok(teamService.createTeam(createTeamRequestDto, userId));
    }

    //팀 참여
    @PostMapping("{teamId}/join")
    public CommonResponseDto<?> joinTeam(
            @PathVariable(name = "teamId", required = false) Long teamId,
            @UserId UUID userId
    ) {
        teamService.joinTeam(teamId, userId);
        return CommonResponseDto.ok(null);
    }
}
