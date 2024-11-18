package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.service.TestService;
import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final TestService testService;

    @GetMapping("/hello")
    public CommonResponseDto<?> hello() {
        return CommonResponseDto.ok("hello");
    }

    @PostMapping("/signin/{name}")
    public CommonResponseDto<?> signIn(
            @PathVariable("name") String name
    ) {
        log.info("name: {}", name);
        return CommonResponseDto.ok(testService.signIn(name, EUserRole.USER));
    }

    @GetMapping("")
    public CommonResponseDto<?> signUp(
            @UserId UUID userId
    ) {
        log.info("uuid : " + userId);
        return CommonResponseDto.ok(true);
    }
}
