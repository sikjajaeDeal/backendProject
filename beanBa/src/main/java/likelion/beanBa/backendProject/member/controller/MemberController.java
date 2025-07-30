package likelion.beanBa.backendProject.member.controller;

import likelion.beanBa.backendProject.member.dto.MemberRequest;
import likelion.beanBa.backendProject.member.dto.MemberResponse;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.email.service.EmailAuthService;
import likelion.beanBa.backendProject.member.email.service.EmailService;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final EmailAuthService emailAuthService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(
            @CurrentUser CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PostMapping("/me")
    public ResponseEntity<MemberResponse> updateMyInfo(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody MemberRequest request) {
        Long memberPk = userDetails.getMember().getMemberPk();
        return ResponseEntity.ok(memberService.updateMember(memberPk, request));
    }

    @PostMapping("/findId")
    public ResponseEntity<String> findId(
            @RequestParam String email) {
        System.out.println(email);
        emailService.sendVerificationCode(email, null,"findId");
        return ResponseEntity.ok("가입시 이메일로 아이디 발송.");
    }

    @PostMapping("/findPassword")
    public ResponseEntity<String> findPassword(
            @RequestParam String memberId,
            @RequestParam String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("검색된 회원이 없습니다."));
        if(!member.getMemberId().equals(memberId)) return ResponseEntity.badRequest().body("아이디와 이메일이 일치하지 않습니다.");

        emailService.sendVerificationCode(email, memberId,"findPassword");
        return ResponseEntity.ok("가입시 이메일로 아이디 발송.");
    }

    @GetMapping("/findPassword/verify")
    public ResponseEntity<String> emailVerify(
            @RequestParam String email,
            @RequestParam String memberId,
            @RequestParam String code) {
        boolean verified = emailService.verifyCode(email, memberId, "findPassword", code);
        if(verified) {
            emailAuthService.markEmailAsVerified(email);
            return ResponseEntity.ok("이메일 인증 성공");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증 실패");
        }
    }

    @PostMapping("/changePwd")
    public ResponseEntity<String> changePwd(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody MemberRequest request) {

        Long memberPk = userDetails.getMember().getMemberPk();
        memberService.updateMember(memberPk, request);

        return ResponseEntity.ok("비밀번호 변경 완료");
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<String> deleteMember(
            @CurrentUser CustomUserDetails userDetails) {
        Long memberPk = userDetails.getMember().getMemberPk();
        return ResponseEntity.ok("탈퇴 완료.");
    }
}
