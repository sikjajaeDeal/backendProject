package likelion.beanBa.backendProject.product.dto;

import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder

/**관리자 페이지 상품 목록 (기존 salePostSummaryResponse에 deleteYn 추가)**/
public class AdminSalePostSummaryResponse {

    private Long postPk;
    private String sellerNickname;
    private String buyerNickname;
    private String categoryName;

    private String title;
    private String content;
    private int hopePrice;
    private Long viewCount;
    private int likeCount;

    private LocalDateTime postAt;
    private LocalDateTime stateAt;
    private SaleStatement state;
    private Yn deleteYn;

    private Double latitude;
    private Double longitude;

    private String thumbnailUrl;

    // 찜 여부 필드 추가
    private boolean salePostLiked;


    public static AdminSalePostSummaryResponse from(SalePost salePost, String thumbnailUrl, boolean salePostLiked, int likeCount) {
        return AdminSalePostSummaryResponse.builder()
                .postPk(salePost.getPostPk())
                .sellerNickname(salePost.getSellerPk().getNickname())
                .buyerNickname(salePost.getBuyerPk() != null ? salePost.getBuyerPk().getNickname() : null)
                .categoryName(salePost.getCategoryPk().getCategoryName())
                .title(salePost.getTitle())
                .content(salePost.getContent())
                .viewCount(salePost.getViewCount())
                .likeCount(likeCount)
                .hopePrice(salePost.getHopePrice())
                .postAt(salePost.getPostAt())
                .stateAt(salePost.getStateAt())
                .state(salePost.getState())
                .deleteYn(salePost.getDeleteYn())
                .latitude(salePost.getLatitude())
                .longitude(salePost.getLongitude())
                .thumbnailUrl(thumbnailUrl)
                .salePostLiked(salePostLiked)
                .build();
    }

}
