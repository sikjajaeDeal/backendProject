package likelion.beanBa.backendProject.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.beanBa.backendProject.chatting.dto.ChattingMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            String msgBody = new String(message.getBody());
            ChattingMessageDto chattingMessageDto = objectMapper.readValue(msgBody, ChattingMessageDto.class);

            if (chattingMessageDto.getTo() != null && !chattingMessageDto.getTo().isEmpty()) {
                // 귓속말
                simpMessagingTemplate.convertAndSendToUser(chattingMessageDto.getTo(), "/queue/private", chattingMessageDto);
            } else {
                // 일반 메시지
                simpMessagingTemplate.convertAndSend("/topic/room." + chattingMessageDto.getRoomId(), chattingMessageDto);


            }
        } catch (Exception e) {

        }

    }
}
