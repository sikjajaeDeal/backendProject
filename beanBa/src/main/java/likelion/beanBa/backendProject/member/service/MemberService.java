package likelion.beanBa.backendProject.member.service;

import likelion.beanBa.backendProject.member.dto.SignupRequest;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest request) {
        if (memberRepository.existsByMemberId(request.getMemberId())) {
            throw new IllegalArgumentException("이미 사용 중인 ID 입니다.");
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 Email 입니다.");
        }

        Member member = Member.builder()
                .memberId(request.getMemberId())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .provider("R")
                .useYn("Y")
                .deleteYn("N")
                .role("member")
                .build();

        memberRepository.save(member);
    }
}
