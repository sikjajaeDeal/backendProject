package likelion.beanBa.backendProject.admin.service;



import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.dto.AdminMemberDTO;
import likelion.beanBa.backendProject.member.dto.MemberResponse;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.member.service.MemberService;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;


    /** 사용자 전체 조회 **/
    public PageResponse<AdminMemberDTO> getAllMembers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findAll(pageable);

        // 기존의 MemberResponse를 사용하여 반환
        Page<AdminMemberDTO> responsePage = memberPage.map(AdminMemberDTO::from);

        System.out.println("토탈 페이지 : "+memberPage.getTotalPages());
        return PageResponse.from(responsePage);
    }

    public PageResponse<AdminMemberDTO>memberSearchId(String memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Member> memberPage = memberRepository.findByMemberId(memberId,pageable);

        Page<AdminMemberDTO> responsePage = memberPage.map(AdminMemberDTO::from);

        System.out.println("🔍 검색 결과 수: " + memberPage.getTotalElements());

        return PageResponse.from(responsePage);


    }

    public PageResponse<AdminMemberDTO>memberSearchNickName(String nickName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Member> memberPage = memberRepository.findByNickname(nickName,pageable);

        Page<AdminMemberDTO> responsePage = memberPage.map(AdminMemberDTO::from);

        System.out.println("🔍 검색 결과 수: " + memberPage.getTotalElements());

        return PageResponse.from(responsePage);
    }

    public PageResponse<AdminMemberDTO>memberSearchEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Member> memberPage = memberRepository.findByEmail(email,pageable);

        Page<AdminMemberDTO> responsePage = memberPage.map(AdminMemberDTO::from);

        System.out.println("🔍 검색 결과 수: " + memberPage.getTotalElements());

        return PageResponse.from(responsePage);
    }


    /** 관리자 멤버 삭제(다중 삭제, 체크박스로 선택한 유저 삭제) 단건은 추후 고려**/
    @Transactional
    public void deleteMembersAdmin(List<Long> memberPkList){
        List<Member> memberList = memberRepository.findAllById(memberPkList);

        if(memberList.isEmpty()){
            throw new IllegalArgumentException("해당 회원들이 없습니다.");
        }

        if(memberList.size() != memberPkList.size()){
            throw new IllegalArgumentException("일부 회원이 없습니다.");
        }

        //실제 삭제가 아닌 DB 상태만 변경
        for(Member member : memberList){
            member.setUseYn("N");
            member.setDeleteYn("Y");
        }

    }

    /**관리자 유저 수정**/
    @Transactional
    public void updateMembersAdmin(List<AdminMemberDTO> memberPkList){
        System.out.println("<UNK> updateMembersAdmin <UNK>");
        for(AdminMemberDTO adminMemberDTO : memberPkList){
            Optional<Member> memberOptional =  memberRepository.findById(adminMemberDTO.getMemberPk());

            if(memberOptional.isEmpty()){
                System.out.println("멤버가 비었습니다");
                throw new IllegalArgumentException("해당 멤버가 존재하지 않습니다.");
            }




            Member member = memberOptional.get();

            boolean isDeleted = "Y".equals(member.getDeleteYn());

            // 삭제된 유저는 deleteYn만 수정 가능
            if (isDeleted) {
                if (adminMemberDTO.getDeleteYn() != null) {
                    member.setDeleteYn(adminMemberDTO.getDeleteYn());
                    if ("N".equals(adminMemberDTO.getDeleteYn())) {
                        // 복구 시 useYn도 함께 켜주고 싶다면 아래 추가 가능
                        member.setUseYn("Y");
                    }
                }
                continue; // 삭제된 멤버는 다른 필드는 수정 못함
            }

//            if (adminMemberDTO.getEmail() != null) {
//                member.setEmail(adminMemberDTO.getEmail());
//
//            }
//            if (adminMemberDTO.getNickname() != null) {
//                member.setNickname(adminMemberDTO.getNickname());
//            }
            if (adminMemberDTO.getProvider() != null) {
                member.setProvider(adminMemberDTO.getProvider());
            }
            if (adminMemberDTO.getRole() != null) {

                member.setRole(adminMemberDTO.getRole());
            }
            if(adminMemberDTO.getPassword() != null) {
                member.setPassword(adminMemberDTO.getPassword());
            }
            if(adminMemberDTO.getUseYn() != null) {
                member.setUseYn(adminMemberDTO.getUseYn());
            }
            if(adminMemberDTO.getDeleteYn() != null) {
                System.out.println("수정전 yn : "+adminMemberDTO.getDeleteYn());
                member.setDeleteYn(adminMemberDTO.getDeleteYn());
                System.out.println("수정후 yn : "+adminMemberDTO.getDeleteYn());
            }
//            if (adminMemberDTO.getLatitude() != null) {
//                member.setLatitude(adminMemberDTO.getLatitude());
//            }
//            if (adminMemberDTO.getLongitude() != null) {
//                member.setLongitude(adminMemberDTO.getLongitude());
//            }
            memberRepository.save(member);

            System.out.println("작동 확인");

        }


    }


}
