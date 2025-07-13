package likelion.beanBa.backendProject.product.dto;

import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/** 판매글 목록만을 보여주기 위한 dto - 이미지는 썸네일만 **/
@Getter
@Builder
public class SalePostSummaryResponse {

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

    private String thumbnailUrl;

    // 찜 여부 필드 추가
    private boolean salePostLiked;


    public static SalePostSummaryResponse from(SalePost salePost, String thumbnailUrl, boolean salePostLiked) {
        return SalePostSummaryResponse.builder()
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
                .thumbnailUrl(thumbnailUrl)
                .salePostLiked(salePostLiked)
                .build();
    }
}
