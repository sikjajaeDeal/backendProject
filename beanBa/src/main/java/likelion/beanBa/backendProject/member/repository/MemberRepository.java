package likelion.beanBa.backendProject.member.repository;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberPk(Long memberPk);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByEmailAndProvider(String email, String provider);
    boolean existsByEmail(String email);
    boolean existsByMemberId(String memberId);
    List<Member> findByDeleteYn(String deleteYn);

    /** 관리자 memberId로 특정 멤버 검색**/
    Page<Member> findByMemberIdContaining(String memberId, Pageable pageable);
    Page<Member> findByNicknameContaining(String nickname, Pageable pageable);
    Page<Member> findByEmailContaining(String email, Pageable pageable);
}
