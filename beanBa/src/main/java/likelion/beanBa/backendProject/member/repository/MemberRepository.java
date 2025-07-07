package likelion.beanBa.backendProject.member.repository;

import likelion.beanBa.backendProject.member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberId(String memberId);
    boolean existByEmail(String email);
    boolean existByMemberId(String memberId);
}
