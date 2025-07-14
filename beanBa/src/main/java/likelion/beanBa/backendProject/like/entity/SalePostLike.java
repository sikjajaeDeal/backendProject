package likelion.beanBa.backendProject.like.entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.entity.SalePost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale_post_like")
public class SalePostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_post_like_pk")
    private Long salePostLikePk;

    /** === 연관관계 매핑 == **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_pk", nullable = false)
    private SalePost postPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_pk", nullable = false)
    private Member memberPk;

    /** === 일반 컬럼 == **/
    private LocalDateTime likedAt;

    public static SalePostLike of(Member member, SalePost salePost) {
        return SalePostLike.builder()
                .memberPk(member)
                .postPk(salePost)
                .likedAt(LocalDateTime.now())
                .build();
    }

}
