package likelion.beanBa.backendProject.member.auth.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.member.auth.dto.*;
import likelion.beanBa.backendProject.member.auth.service.AuthService;
import likelion.beanBa.backendProject.member.dto.SignupRequest;
import likelion.beanBa.backendProject.member.email.service.EmailAuthService;
import likelion.beanBa.backendProject.member.email.service.EmailService;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;
    private final EmailService emailService;
    private final EmailAuthService emailAuthService;
    private final MemberRepository memberRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody SignupRequest request) {

        if(!emailAuthService.isEmailVerified(request.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이메일 인증을 먼저 진행해주세요.");
        }

        memberService.signup(request);
        emailAuthService.clearVerified(request.getEmail());
        return ResponseEntity.ok("회원 가입 완료.");
    }

    @PostMapping("/signup/email")
    public ResponseEntity<String> sendEmailVerification(
            @RequestParam String email) {
        if(memberRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일 입니다.");
        }
        emailService.sendVerificationCode(email, null,"signup");
        return ResponseEntity.ok("이메일 인증 메일을 전송했습니다.");
    }

    @GetMapping("/signup/verify")
    public ResponseEntity<String> emailVerify(
            @RequestParam String email,
            @RequestParam String token) {
        boolean verified = emailService.verifyCode(email, token);
        if(verified) {
            emailAuthService.markEmailAsVerified(email);
            return ResponseEntity.ok("이메일 인증 성공");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증 실패");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtToken> refresh (
            @RequestBody RefreshTokenRequest request) {
        JwtToken token = authService.reissue(request);
        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @CurrentUser CustomUserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
