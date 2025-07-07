package likelion.beanBa.backendProject.member.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.member.dto.SignupRequestDTO;
import likelion.beanBa.backendProject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody @Valid SignupRequestDTO request) {
        memberService.signup(request);
        return ResponseEntity.ok("회원 가입 완료.");
    }
}
