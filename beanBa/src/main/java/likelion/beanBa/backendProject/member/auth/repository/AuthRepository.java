package likelion.beanBa.backendProject.member.auth.repository;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.auth.Entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByMemberAndDeleteYn(Member member, String deleteYn);

    Optional<Auth> findByRefreshTokenAndDeleteYn(String refreshToken, String deleteYn);

    Optional<Auth> findByMemberAndRefreshTokenNotAndDeleteYn(Member member, String refreshToken, String deleteYn);
}
