package likelion.beanBa.backendProject.product.dto;

import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SalePostResponse {

    private Long id;
    private String sellerNickname;
    private String categoryName;

    private String title;
    private String content;
    private int hopePrice;
    private Long viewCount;

    private LocalDateTime postAt;
    private LocalDateTime stateAt;
    private SaleStatement state;
//    private Yn deleteYn;

    private Double latitude;
    private Double longitude;

    private List<String> imageUrls;

    public static SalePostResponse from(SalePost post, List<String> imageUrls) {
        return SalePostResponse.builder()
                .id(post.getPostPk())
                .sellerNickname(post.getSellerPk().getNickname())
                .categoryName(post.getCategoryPk().getCategoryName())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .hopePrice(post.getHopePrice())
                .postAt(post.getPostAt())
                .stateAt(post.getStateAt())
                .state(post.getState())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .imageUrls(imageUrls)
                .build();
    }
}