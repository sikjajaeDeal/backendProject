package likelion.beanBa.backendProject.member.email.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.jwt.JwtTokenProvider;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final Map<String, String> emailCodeMap = new ConcurrentHashMap<>();

    public void sendVerificationCode(String email, String memberId, String purpose) {
        String token = null;

        if(!purpose.equals("findPassword")) {
            token = jwtTokenProvider.generateEmailToken(email);
            email = email.trim().toLowerCase();
            emailCodeMap.put(email, token);
        } else {
            token = jwtTokenProvider.generateTokenForPwd(memberId);
            emailCodeMap.put(memberId, token);
        }

        SimpleMailMessage message = new SimpleMailMessage();

        switch (purpose) {
            case "signup" -> {
                message.setTo(email);
                message.setSubject("콩바구니 회원가입 이메일 인증");
                String verifyUrl = "https://localhost:8081/signup/verify?email=" + email + "&token=" + token;
                message.setText("콩바구니 회원가입을 위해 아래 링크를 클릭해주세요:\n" + verifyUrl);
            }
            case "findId" -> {
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("검색된 회원이 없습니다."));
                message.setTo(email);
                message.setSubject("콩바구니 아이디 찾기");
                message.setText("해당 " + email + " 로 가입하신 아이디는 " + member.getMemberId() + " 입니다.");
            }
            case "findPassword" -> {
                message.setTo(email);
                message.setSubject("콩바구니 비밀번호 변경");
                String changePwdUrl = "https://localhost:8081/changePwd?memberId=" + memberId + "&token=" + token;
                message.setText("콩바구니 회원가입을 위해 아래 링크를 클릭해주세요:\n" + changePwdUrl);
            }
            default -> throw new IllegalArgumentException("알 수 없는 purpose 입니다.");
        }
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
