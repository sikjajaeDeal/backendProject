package likelion.beanBa.backendProject.chatting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.beanBa.backendProject.chatting.dto.ChattingMessageDto;
import likelion.beanBa.backendProject.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChattingController {

    // redispublisher
    private final RedisPublisher redisPublisher;
    // json<->dto 를 위한 객체
    private ObjectMapper objectMapper = new ObjectMapper();

    // (방이 없을 시)동적으로 방 생성 및 채팅
    @MessageMapping("/chat.sendMessage")
    public void sendmessage(ChattingMessageDto message) throws JsonProcessingException {
//        message.setMessage(instantname+" "+message.getMessage());

        /*
        if(message.getTo() != null && !message.getTo().isEmpty() ) {
            // 귓속말
            // 내 아이디로 귓속말 경로를 활성화함
            template.convertAndSendToUser(message.getTo(), "/queue/private",message);
        } else {
            // 일반 메시지
            // message에서 roomId를 추출해서 해당 roomId를 구독하고 있는 클라이언트에게 메시지를 전달
            template.convertAndSend("/topic/"+message.getRoomId(), message);
        }
        */

        String channel = null;
        String msg = null;

        if (message.getTo() != null && !message.getTo().isEmpty()) {
            // 귓속말
            //내 아이디로 귓속말경로를 활성화 함
            channel = "private."+message.getRoomId();
            msg = objectMapper.writeValueAsString(message);

        } else {
            // 일반 메시지
            channel = "room."+message.getRoomId();
            msg = objectMapper.writeValueAsString(message);
        }

        redisPublisher.publish(channel, msg);
    }
}
