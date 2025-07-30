package likelion.beanBa.backendProject.member.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SIGNUP_PREFIX = "email : signup : ";
    private static final long VERIFIED_TTL = 5;

    public void markEmailAsVerified(String email) {
        String key = SIGNUP_PREFIX + email;
        redisTemplate.opsForValue()
                .set(key, "VERIFIED", VERIFIED_TTL, TimeUnit.MINUTES);
    }

    public boolean isEmailVerified(String email){
        String key = SIGNUP_PREFIX+email;
        return redisTemplate.hasKey(key);
    }

    public void clearVerified(String email) {
        String key = SIGNUP_PREFIX + email;
        redisTemplate.delete(key);
    }

}
