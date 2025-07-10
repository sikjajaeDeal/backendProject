package likelion.beanBa.backendProject.member.controller;

import likelion.beanBa.backendProject.member.dto.MemberRequest;
import likelion.beanBa.backendProject.member.dto.MemberResponse;
import likelion.beanBa.backendProject.member.Entity.Member;
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

    @DeleteMapping("/deleteMember")
    public ResponseEntity<String> deleteMember(
            @CurrentUser CustomUserDetails userDetails) {
        Long memberPk = userDetails.getMember().getMemberPk();
        return ResponseEntity.ok("탈퇴 완료.");
    }
}
