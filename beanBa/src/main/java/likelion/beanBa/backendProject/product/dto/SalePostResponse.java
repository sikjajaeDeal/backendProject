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

    private Long postPk;
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

    public static SalePostResponse from(SalePost salePost, List<String> imageUrls) {
        return SalePostResponse.builder()
                .postPk(salePost.getPostPk())
                .sellerNickname(salePost.getSellerPk().getNickname())
                .categoryName(salePost.getCategoryPk().getCategoryName())
                .title(salePost.getTitle())
                .content(salePost.getContent())
                .viewCount(salePost.getViewCount())
                .hopePrice(salePost.getHopePrice())
                .postAt(salePost.getPostAt())
                .stateAt(salePost.getStateAt())
                .state(salePost.getState())
                .latitude(salePost.getLatitude())
                .longitude(salePost.getLongitude())
                .imageUrls(imageUrls)
                .build();
    }
}