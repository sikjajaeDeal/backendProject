package likelion.beanBa.backendProject.member.email.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.jwt.JwtTokenProvider;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final Map<String, String> emailCodeMap = new ConcurrentHashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;

    private static final SecureRandom random = new SecureRandom();
    private static final long CODE_TTL = 5;

    public void sendVerificationCode(String email, String memberId, String purpose) {

        String code  = randomCode(6);
        String key = buildKey(purpose, email, memberId);

        redisTemplate.opsForValue()
                .set(key, code, CODE_TTL, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);

        switch (purpose) {
            case "signup" :
                message.setSubject("콩바구니 회원가입 이메일 인증");
                message.setText("회원 가입 인증 코드 : " + code + "\n\n 5분 내 입력해주세요.");
                break;
            case "findId" :
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("검색된 회원이 없습니다."));
                message.setSubject("콩바구니 아이디 찾기");
                message.setText("가입 아이디 : " +member.getMemberId() );
                break;
            case "findPassword" :
                message.setSubject("콩바구니 비밀번호 재설정 인증 코드");
                message.setText("비밀번호 재설정 인증 코드 : " + code + "\n\n 5분 내 입력해주세요.");
                break;
            default: throw new IllegalArgumentException("Unknown purpose : "+purpose);
        }
        javaMailSender.send(message);

//        if(!purpose.equals("findPassword")) {
//            token = jwtTokenProvider.generateEmailToken(email);
//            email = email.trim().toLowerCase();
//            emailCodeMap.put(email, token);
//        } else {
//            token = jwtTokenProvider.generateTokenForPwd(memberId);
//            emailCodeMap.put(memberId, token);
//        }
//
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        switch (purpose) {
//            case "signup" -> {
//                message.setTo(email);
//                message.setSubject("콩바구니 회원가입 이메일 인증");
//                String verifyUrl = "https://beanba.store/signup/verify?email=" + email + "&token=" + token;
//                message.setText("콩바구니 회원가입을 위해 아래 링크를 클릭해주세요:\n" + verifyUrl);
//            }
//            case "findId" -> {
//                Member member = memberRepository.findByEmail(email)
//                        .orElseThrow(() -> new IllegalArgumentException("검색된 회원이 없습니다."));
//                message.setTo(email);
//                message.setSubject("콩바구니 아이디 찾기");
//                message.setText("해당 " + email + " 로 가입하신 아이디는 " + member.getMemberId() + " 입니다.");
//            }
//            case "findPassword" -> {
//                message.setTo(email);
//                message.setSubject("콩바구니 비밀번호 변경");
//                String changePwdUrl = "https://beanba.store/changePwd?memberId=" + memberId + "&token=" + token;
//                message.setText("콩바구니 회원가입을 위해 아래 링크를 클릭해주세요:\n" + changePwdUrl);
//            }
//            default -> throw new IllegalArgumentException("알 수 없는 purpose 입니다.");
//        }
//        javaMailSender.send(message);
    }

    public boolean verifyCode(String email, String memberId, String purpose, String submittedCode) {
        String key = buildKey(purpose, email, memberId);
        Object cached = redisTemplate.opsForValue().get(key);

        if(cached != null && cached.equals(submittedCode)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

//    public boolean oldVerifyCode(String email, String token) {
//        email = email.trim().toLowerCase();
//        String storedToken = emailCodeMap.get(email);
//        if(storedToken == null || !storedToken.equals(token)) return false;
//        if(!jwtTokenProvider.validateToken(token)) return false;
//        if(!jwtTokenProvider.getMemberIdFromToken(token).equals(email)) return false;
//
//        emailCodeMap.remove(email);
//        return true;
//    }

    public static String randomCode(int length) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String buildKey(String purpose, String email, String memberId) {
        return switch (purpose) {
            case "signup" -> "email : signup : " + email;
            case "findPassword" -> "email : pwd : " + memberId;
            case "findId" -> "email : id : " + email;
            default -> "email : code : " + email;
        };
    }
}
