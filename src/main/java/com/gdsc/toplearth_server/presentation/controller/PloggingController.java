package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plogging")
@Slf4j
@RequiredArgsConstructor
public class PloggingController {
    @PostMapping("/start")
    public CommonResponseDto<?> startPlogging() {
        return CommonResponseDto.ok("플로깅 시작");
    }


}
