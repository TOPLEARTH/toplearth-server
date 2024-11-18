package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.service.TestService;
import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final TestService testService;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/signin/{name}")
    public CommonResponseDto<?> signIn(
            @PathVariable("name") String name
    ) {
        log.info("name: {}", name);
        return CommonResponseDto.ok(testService.signIn(name, EUserRole.USER));
    }

    @PostMapping("/signin/admin/{name}")
    public CommonResponseDto<?> signInAdmin(
            @PathVariable("name") String name
    ) {
        log.info("name: {}", name);
        return CommonResponseDto.ok(testService.signInAdmin(name, EUserRole.ADMIN));
    }

    @GetMapping("")
    public CommonResponseDto<?> signUp(
            @UserId UUID userId
    ) {
        log.info("uuid : " + userId);
        return CommonResponseDto.ok(true);
    }
}
