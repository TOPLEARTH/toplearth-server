package com.gdsc.toplearth_server.presentation.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
    @MessageMapping("/connect")
    @SendTo("/topic/connect")
    public String sendWelcomeMessage() {
        return "도형 hi ㅋㅋ :)";
    }
}
