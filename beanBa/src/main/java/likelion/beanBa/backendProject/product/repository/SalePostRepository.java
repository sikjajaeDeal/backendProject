package likelion.beanBa.backendProject.product.repository;


import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.TopPostSummaryProjection;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface SalePostRepository extends JpaRepository<SalePost, Long> {

    //삭제되지 않은 게시글 단건 조회
    Optional<SalePost> findByPostPkAndDeleteYn(Long postPk, Yn deleteYn);

    // 삭제되지 않은 게시글 전체 조회
    List<SalePost> findAllByDeleteYn(Yn deleteYn);

    // 페이징 적용한 삭제되지 않은 게시글 전체 조회
    Page<SalePost> findAllByDeleteYn(Yn deleteYn, Pageable pageable);

    // 내가 판매한 글 - 최신순 정렬
    List<SalePost> findAllBySellerPkAndDeleteYnOrderByPostAtDesc(Member member, Yn deleteYn);

    // 내가 구매한 글 - 상태가 C(판매완료) 이고 최신순 정렬
    List<SalePost> findAllByBuyerPkAndStateAndDeleteYnOrderByPostAtDesc(Member member, SaleStatement state, Yn deleteYn);

    @Query("SELECT s FROM SalePost s WHERE s.postPk IN :ids")
    List<SalePost> findAllByPostPks(List<Long> ids);

    //내가 판매한 글 - 페이징에서 최신순 정렬
    Page<SalePost> findAllBySellerPkAndDeleteYn(Member seller, Yn deleteYn, Pageable pageable);

    //내가 구매한 글 - 상태가 C(판매완료) 이고 페이징에서 최신순 정렬
    Page<SalePost> findAllByBuyerPkAndStateAndDeleteYn(Member buyer, SaleStatement state, Yn deleteYn, Pageable pageable);


    @Query("""
        SELECT 
            p.postPk AS postPk,
            m.nickname AS sellerNickname,
            c.categoryName AS categoryName,
            p.title AS title,
            p.content AS content,
            p.hopePrice AS hopePrice,
            p.viewCount AS viewCount,
            COUNT(l) AS likeCount,
            p.postAt AS postAt,
            p.stateAt AS stateAt,
            p.state AS state,
            p.latitude AS latitude,
            p.longitude AS longitude
        FROM SalePost p
        JOIN p.sellerPk m
        JOIN p.categoryPk c
        LEFT JOIN SalePostLike l ON l.postPk = p
        WHERE p.deleteYn = 'N' AND p.state = 'S'
        GROUP BY p.postPk, m.nickname, c.categoryName, p.title, p.content, p.hopePrice,
                 p.viewCount, p.postAt, p.stateAt, p.state, p.latitude, p.longitude
        ORDER BY COUNT(l) DESC, p.viewCount DESC
    """)
    List<TopPostSummaryProjection> findTop4SalePostsByLikeAndView(Pageable pageable);




    // 특정 멤버의 블라인드 된 글 갯수 반환
    long countBySellerPk_MemberPkAndDeleteYn(Long memberPk, Yn deleteYn);
}
