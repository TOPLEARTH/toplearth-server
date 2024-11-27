package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.service.FcmService;
import com.gdsc.toplearth_server.application.service.TestService;
import com.gdsc.toplearth_server.application.service.team.TeamService;
import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
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
    private final TeamService teamService;
    private final FcmService fcmService;
    private final UserRepositoryImpl userRepository;

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

    @GetMapping("/quest")
    public CommonResponseDto<?> quest(
            @UserId UUID userId
    ) {
        return CommonResponseDto.ok(teamService.readTeam(userId));
    }

    @GetMapping("/home")
    public CommonResponseDto<?> home(
            @UserId UUID userId
    ) {
        return CommonResponseDto.ok(testService.getHomeInfo(userId));
    }

    @PostMapping("/fcm")
    public CommonResponseDto<?> fcm(
            @UserId UUID userId
    ) {
        User user = userRepository.findById(userId).orElse(null);
        System.err.println("asdfasd.  " + user.getFcmToken());
        fcmService.sendMessage("이도형이ㅇㅇ형", "성공한건가요...? ",
                "emHZ1inZGkELhzPqpZgtyR:APA91bFRL9-vqJkAK_vzwQ1m6ACrjyfrLFpGMXv_3Ct8RfF9j8H3REizQRVUBhtCj201rqkQmK9pHWMMyRryRaxB_OVeW9WLNcKXOsUhSA4E4Z0FxqKHoWo");
        return CommonResponseDto.ok(true);
    }
}
