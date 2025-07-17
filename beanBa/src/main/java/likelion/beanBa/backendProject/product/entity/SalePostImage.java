package likelion.beanBa.backendProject.product.entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "sale_post_image")
public class SalePostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_pk")
    private Long imagePk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_pk", nullable = false)
    private SalePost postPk;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_yn", nullable = false, length = 1)
    private Yn deleteYn;

    @Column(name = "image_order")
    private Integer imageOrder;

    // 정적 팩토리 메서드
    public static SalePostImage ofWithOrder(SalePost salePost, String imageUrl, int imageOrder) {
        return SalePostImage.builder()
                .postPk(salePost)
                .imageUrl(imageUrl)
                .deleteYn(Yn.N)
                .imageOrder(imageOrder)
                .build();
    }

    // 삭제 처리 메서드(deleteYn 상태만 변경)
    public void markAsDeleted() {
        this.deleteYn = Yn.Y;
    }
}