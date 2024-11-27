package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.service.admin.AdminService;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    //회원 상세조회 (피그마 완성하면 할 예정)
    @GetMapping("/users/{userId}")
    public CommonResponseDto<?> getUser(
            @PathVariable UUID userId
    ) {
        return CommonResponseDto.ok(adminService.getUser(userId));
    }

    //회원 조회 및 검색
    @GetMapping("/users/search")
    public CommonResponseDto<?> searchUser(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "sort", defaultValue = "nickname") String sort,
            @RequestParam(value = "text", required = false) String text
    ) {
        return CommonResponseDto.ok(adminService.searchUser(page, size, sort, text));
    }

    //신고 조회
    @GetMapping("/reports")
    public CommonResponseDto<?> getReports(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort
    ) {
        return CommonResponseDto.ok(adminService.getReports(page, size, sort));
    }

    //신고 처리
    @PatchMapping("/reports/{reportId}")
    public CommonResponseDto<?> updateReport(
            @PathVariable Long reportId
    ) {
        return CommonResponseDto.ok(adminService.updateReport(reportId));
    }

    //팀 조회
    @GetMapping("/teams")
    public CommonResponseDto<?> getTeams(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "text", required = false) String text
    ) {
        return CommonResponseDto.ok(adminService.getTeams(page, size, sort, text));
    }

    //팀 상세 조회
    @GetMapping("/teams/{teamId}")
    public CommonResponseDto<?> getTeam(
            @PathVariable Long teamId
    ) {
        return CommonResponseDto.ok(adminService.getTeam(teamId));
    }

}
