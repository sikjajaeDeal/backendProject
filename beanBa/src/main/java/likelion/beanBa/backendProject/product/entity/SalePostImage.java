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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_pk", nullable = false)
    private SalePost post;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_yn", nullable = false, length = 1)
    private Yn deleteYn;

    // 정적 팩토리 메서드
    public static SalePostImage of(SalePost post, String imageUrl) {
        return SalePostImage.builder()
                .post(post)
                .imageUrl(imageUrl)
                .deleteYn(Yn.N)
                .build();
    }

    // 삭제 처리 메서드(deleteYn 상태만 변경)
    public void markAsDeleted() {
        this.deleteYn = Yn.Y;
    }
}