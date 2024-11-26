package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.service.PloggingService;
import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.presentation.request.plogging.CreatePloggingRequestDto;
import com.gdsc.toplearth_server.presentation.request.plogging.UpdatePloggingRequestDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/plogging")
@Slf4j
@RequiredArgsConstructor
public class PloggingController {
    private final PloggingService ploggingService;

    // 유저 개인 플로깅 시작
    @PostMapping("/start")
    public CommonResponseDto<?> createUserPlogging(
            @UserId UUID userId,
            @RequestBody CreatePloggingRequestDto createPloggingRequestDto
    ) {
        ploggingService.createUserPlogging(userId, createPloggingRequestDto);
        return CommonResponseDto.ok(null);
    }

    // 유저 플로깅 인증 사진 업로드
    @PostMapping(value = "/{ploggingId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseDto<?> createPloggingImage(
            @UserId UUID userId,
            @PathVariable Long ploggingId,
            @RequestParam(value = "ploggingImage") MultipartFile profileImage,
            @RequestParam(value = "latitude") Double latitude,
            @RequestParam(value = "longitude") Double longitude
    ) {
        ploggingService.createPloggingImage(userId, ploggingId, profileImage, latitude, longitude);
        return CommonResponseDto.ok(null);
    }

    // 유저 플로깅 종료
    @PatchMapping("/{ploggingId}")
    public CommonResponseDto<?> updatePlogging(
            @UserId UUID userId,
            @PathVariable Long ploggingId,
            @RequestBody UpdatePloggingRequestDto updatePloggingRequestDto
    ) {
        ploggingService.updatePlogging(userId, ploggingId, updatePloggingRequestDto);
        return CommonResponseDto.ok(null);
    }
}
