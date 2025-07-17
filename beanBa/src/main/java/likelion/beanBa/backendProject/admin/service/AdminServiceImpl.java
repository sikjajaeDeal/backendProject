package likelion.beanBa.backendProject.admin.service;


import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.dto.MemberResponse;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;

    /** 사용자 전체 조회 **/
    public PageResponse<MemberResponse> getAllMembers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findAll(pageable);

        // 기존의 MemberResponse를 사용하여 반환
        Page<MemberResponse> responsePage = memberPage.map(MemberResponse::from);

        System.out.println("토탈 페이지 : "+memberPage.getTotalPages());
        return PageResponse.from(responsePage);
    }
}
