package likelion.beanBa.backendProject.member.service;

import likelion.beanBa.backendProject.member.dto.MemberRequest;
import likelion.beanBa.backendProject.member.dto.MemberResponse;
import likelion.beanBa.backendProject.member.dto.SignupRequest;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public MemberResponse updateMember(Long memberPk, MemberRequest request) {
        Member member = memberRepository.findByMemberPk(memberPk)
                .orElseThrow(() -> new IllegalArgumentException("검색된 회원이 없습니다."));
        if(request.getNickname() != null&& !request.getNickname().isEmpty()) {
            member.setNickname(request.getNickname());
        }
        if(request.getPassword() != null&& !request.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if(request.getLatitude() != null) {
            member.setLatitude(request.getLatitude());
        }
        if(request.getLongitude() != null) {
            member.setLongitude(request.getLongitude());
        }
        return MemberResponse.from(member);
    }

    @Transactional
    public void deleteMember(Long memberPk) {
        Member member = memberRepository.findByMemberPk(memberPk)
                .orElseThrow(() -> new IllegalArgumentException("검색된 회원이 없습니다."));
        member.setUseYn("N");
        member.setDeleteYn("Y");
    }
}
