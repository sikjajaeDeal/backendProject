package likelion.beanBa.backendProject.product.entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
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
    @JoinColumn(name = "member_pk", nullable = false)
    private Member seller;

    //카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_pk", nullable = false)
    private Category category;


    //구매자 PK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_member_pk", nullable = true)
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
    private LocalDateTime postAt;

    @Enumerated(EnumType.STRING)
    private SaleStatement state = SaleStatement.S;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_yn")
    private Yn deleteYn = Yn.N;

    @Column(nullable = false)
    private LocalDateTime stateAt;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    public static SalePost create(Member seller, Category category, String title, String content,
                                  Integer hopePrice, double latitude, double longitude) {

        LocalDateTime now = LocalDateTime.now();

        return SalePost.builder()
                .seller(seller)
                .category(category)
                .title(title)
                .content(content)
                .hopePrice(hopePrice)
                .viewCount(0L)
                .state(SaleStatement.S)
                .deleteYn(Yn.N)
                .postAt(now)
                .stateAt(now)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public void update(String title, String content, int hopePrice, double latitude, double longitude, Category category) {
        this.title = title;
        this.content = content;
        this.hopePrice = hopePrice;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.stateAt = LocalDateTime.now();
    }

    public void markAsDeleted() {
        this.deleteYn = Yn.Y;
    }

}
