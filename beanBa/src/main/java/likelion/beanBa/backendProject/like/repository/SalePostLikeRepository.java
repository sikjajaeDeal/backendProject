package likelion.beanBa.backendProject.like.repository;

import likelion.beanBa.backendProject.like.entity.SalePostLike;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.entity.SalePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SalePostLikeRepository extends JpaRepository<SalePostLike, Long> {

    // 사용자의 찜 여부 반환
    boolean existsByMemberPkAndPostPk(Member member, SalePost post);

    // 찜 취소 시, 객체 삭제를 위해 객체를 반환
    Optional<SalePostLike> findByMemberPkAndPostPk(Member member, SalePost post);

    // 사용자가 찜한 모든 게시물 반환
    List<SalePostLike> findAllByMemberPk(Member member);

    List<SalePostLike> findAllByMemberPkOrderByLikedAtDesc(Member member);

    // 찜 개수 조회
    int countByPostPk(SalePost salePost);

    @Query("""
    SELECT s 
    FROM SalePostLike l
    JOIN l.postPk s
    WHERE l.memberPk = :member
      AND s.deleteYn = 'N'
    """)
    Page<SalePost> findLikedSalePosts(@Param("member") Member member, Pageable pageable);
}