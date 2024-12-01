package com.gdsc.toplearth_server.infrastructure.message;

import com.gdsc.toplearth_server.core.common.ExceptionDto;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import java.nio.charset.StandardCharsets;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
public class GlobalSocketErrorHandler extends StompSubProtocolErrorHandler {

    public GlobalSocketErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage, Throwable ex)
    {
        ex.printStackTrace();
        Throwable exception = new MessageDeliveryException("abc");
        return handleUnauthorizedException(clientMessage, exception);
    }

    private Message<byte[]> handleUnauthorizedException(Message<byte[]> clientMessage, Throwable ex)
    {

        ExceptionDto exceptionDto = new ExceptionDto(ErrorCode.TOKEN_UNKNOWN);

        return prepareErrorMessage(clientMessage, exceptionDto, String.valueOf(ErrorCode.TOKEN_UNKNOWN.getCode()));

    }

    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, ExceptionDto exceptionDto, String errorCode)
    {

        String message = exceptionDto.getMessage();

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(errorCode);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
