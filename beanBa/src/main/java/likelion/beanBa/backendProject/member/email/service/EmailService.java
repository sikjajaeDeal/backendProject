package likelion.beanBa.backendProject.member.email.service;

import likelion.beanBa.backendProject.member.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;

    private final Map<String, String> emailCodeMap = new ConcurrentHashMap<>();

    public void sendVerificationCode(String email) {
        String token = jwtTokenProvider.generateEmailToken(email);
        emailCodeMap.put(email, token);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("콩바구니 회원가입 이메일 인증");
        message.setText("인증 토큰 : "+token);

        javaMailSender.send(message);
    }

    public boolean verifyCode(String email, String token) {
        String storedToken = emailCodeMap.get(email);
        if(storedToken == null || !storedToken.equals(token)) return false;
        return jwtTokenProvider.validateToken(token) &&
                jwtTokenProvider.getMemberIdFromToken(token).equals(email);
    }
}
