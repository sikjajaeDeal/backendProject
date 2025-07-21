package likelion.beanBa.backendProject.member.repository;

import likelion.beanBa.backendProject.member.Entity.Member;
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
}
