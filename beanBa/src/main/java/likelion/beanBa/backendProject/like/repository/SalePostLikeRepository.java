package likelion.beanBa.backendProject.like.repository;

import likelion.beanBa.backendProject.like.entity.SalePostLike;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.entity.SalePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalePostLikeRepository extends JpaRepository<SalePostLike, Long> {

    boolean existsByMemberPkAndPostPk(Member member, SalePost post);

    Optional<SalePostLike> findByMemberPkAndPostPk(Member member, SalePost post);

    List<SalePostLike> findAllByMemberPk(Member member);
}