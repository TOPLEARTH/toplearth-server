//package com.gdsc.toplearth_server.infrastructure.message;
//
//import com.gdsc.toplearth_server.core.constant.Constants;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Controller;
//
//@Controller
//@RequiredArgsConstructor
//@Slf4j
//public class StompRabbitMessageController {
//    private final RabbitTemplate rabbitTemplate;
//    private final MessageService messageService;
//
//    // 대결방 입장
//    @MessageMapping("matching.enter.{matchingId}")
//    public MessageInfo enterUser(@DestinationVariable("matchingId") Long matchingId,
//                                 @Payload MessageCreateRequest message) {
//        message.setMessage(message.getSender() + "님이 채팅방에 입장하였습니다.");
//        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, "enter.matching." + matchingId, message);
//        return messageService.saveMessage(message);
//    }
//
//    // 대결방에 플로깅 정보 전송
//    @MessageMapping("matching.message.{matchingId}")
//    public MessageInfo talkUser(@DestinationVariable("matchingId") Long matchingId,
//                                @Payload MessageCreateRequest message) {
//        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, "*.matching." + matchingId, message);
//        return messageService.saveMessage(message);
//    }
//
//    // 대결방 퇴장
//    @MessageMapping("matching.exit.{matchingId}")
//    public MessageInfo exitUser(@DestinationVariable("matchingId") Long matchingId,
//                                @Payload MessageCreateRequest message) {
//        message.setMessage(message.getSender() + "님이 채팅방에 퇴장하였습니다.");
//        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, "exit.matching." + matchingId, message);
//        return messageService.saveMessage(message);
//    }
//
//    // Only Debugging
//    @RabbitListener(queues = Constants.MATCHING_QUEUE_NAME)
//    public void receive(TeamInfoMessage teamInfoMessage) {
//        log.info("received : {}", teamInfoMessage);
//    }
//}
