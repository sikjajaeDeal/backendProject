package likelion.beanBa.backendProject.product.entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SalePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_pk")
    private long id;

    //작성자 판매자 PK
    //다대일 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_pk")
    private Member member_pk;

    //카테고리


    //구매자 PK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_member_pk")
    private Member buy_member_pk;


    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob //TEXT 타입으로 만들기
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private Long view_count = 0L; //조회수 기본 값 0으로 초기화

    @Column(name = "hope_price", nullable = false)
    private int hope_price = 0;




}
