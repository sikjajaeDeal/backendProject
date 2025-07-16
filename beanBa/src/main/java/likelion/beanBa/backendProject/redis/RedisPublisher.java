package likelion.beanBa.backendProject.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisPublisher {

    // 메시지를 발행하는 클래스
    private final StringRedisTemplate stringRedisTemplate;

    // stomp -> pub -> sub -> stomp
    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
