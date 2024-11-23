package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.service.BootstrapService;
import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BootstrapController {
    private final BootstrapService bootstrapService;

    @GetMapping("/bootstrap")
    public CommonResponseDto<?> bootstrap(
            @UserId UUID userId
    ) {
        return CommonResponseDto.ok(bootstrapService.bootstrap(userId));
    }
}
