package likelion.beanBa.backendProject.product.entity.sale;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.StaleStateException;

import java.time.LocalDateTime;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale_post")
public class SalePost {

    /** 테이블 PK **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_pk")
    private long id;

    /** 연관관계 매핑 **/
    //작성자 판매자 PK
    //다대일 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_pk")
    private Member seller;

//    //카테고리
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_pk", nullable = false)
//    private Category category;


    //구매자 PK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_member_pk")
    private Member buyer;

    /** 일반 컬럼 **/
    @Column(length = 255, nullable = false)
    private String title;

    @Lob //TEXT 타입으로 만들기
    @Column(nullable = false)
    private String content;


    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L; //조회수 기본 값 0으로 초기화

    @Column(name = "hope_price", nullable = false)
    private int hopePrice = 0;

    @Column(name = "post_at")
    private LocalDateTime postAT;







}
