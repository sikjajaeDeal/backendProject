package likelion.beanBa.backendProject.chatting.websocket.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

import java.util.*;

@Component
@RequiredArgsConstructor
public class StompEventListener {

    private final RedisTemplate<String, String> redisTemplate;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();

        if (user != null) {
            String sessionId = accessor.getSessionId();
            String roomDestination = accessor.getDestination(); // e.g., /topic/room.5

            // 예: key = room.5, value = 사용자ID
            redisTemplate.opsForSet().add(roomDestination, user.getName());
        }
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Principal user = accessor.getUser();

        if (user != null) {
            // 모든 room.* 에서 사용자 제거
            Set<String> keys = redisTemplate.keys("room.*");
            if (keys != null) {
                for (String key : keys) {
                    redisTemplate.opsForSet().remove(key, user.getName());
                }
            }
        }
    }
}
