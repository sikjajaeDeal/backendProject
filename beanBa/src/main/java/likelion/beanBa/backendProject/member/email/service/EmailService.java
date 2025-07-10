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
        email = email.trim().toLowerCase();
        emailCodeMap.put(email, token);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("콩바구니 회원가입 이메일 인증");
        String verifyUrl = "https://localhost:8080/signup/verify?email=" + email + "&token=" + token;
        message.setText("콩바구니 회원가입을 위해 아래 링크를 클릭해주세요:\n" + verifyUrl);

        javaMailSender.send(message);
    }

    public boolean verifyCode(String email, String token) {
        email = email.trim().toLowerCase();
        String storedToken = emailCodeMap.get(email);
        if(storedToken == null || !storedToken.equals(token)) return false;
        if(!jwtTokenProvider.validateToken(token)) return false;
        if(!jwtTokenProvider.getMemberIdFromToken(token).equals(email)) return false;

        emailCodeMap.remove(email);
        return true;
    }
}
