package likelion.beanBa.backendProject.product.dto;

import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsTestDocument;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/** 판매글 목록만을 보여주기 위한 dto - 이미지는 썸네일만 **/
@Getter
@Builder
public class
SalePostSummaryResponse {

    private Long postPk;
    private String sellerNickname;
    private String categoryName;

    private String title;
    private String content;
    private int hopePrice;
    private Long viewCount;
    private int likeCount;

    private LocalDateTime postAt;
    private LocalDateTime stateAt;
    private SaleStatement state;
//    private Yn deleteYn;

    private Double latitude;
    private Double longitude;

    private String thumbnailUrl;

    // 찜 여부 필드 추가
    private boolean salePostLiked;


    public static SalePostSummaryResponse from(SalePost salePost, String thumbnailUrl, boolean salePostLiked, int likeCount) {
        return SalePostSummaryResponse.builder()
                .postPk(salePost.getPostPk())
                .sellerNickname(salePost.getSellerPk().getNickname())
                .categoryName(salePost.getCategoryPk().getCategoryName())
                .title(salePost.getTitle())
                .content(salePost.getContent())
                .viewCount(salePost.getViewCount())
                .likeCount(likeCount)
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

    public static SalePostSummaryResponse from(SalePostEsTestDocument salePostEsTestDocument) {
        return SalePostSummaryResponse.builder()
            .postPk(salePostEsTestDocument.getPostPk())
            .sellerNickname(salePostEsTestDocument.getSellerNickname())
            .categoryName(salePostEsTestDocument.getCategoryName())

            .title(salePostEsTestDocument.getTitle())
            .content(salePostEsTestDocument.getContent())
            .hopePrice(salePostEsTestDocument.getHopePrice())
            .viewCount(salePostEsTestDocument.getViewCount())
            .likeCount(salePostEsTestDocument.getLikeCount())

            .postAt(salePostEsTestDocument.getPostAt())
            .stateAt(salePostEsTestDocument.getStateAt())
            .state(salePostEsTestDocument.getState())

            .latitude(salePostEsTestDocument.getLatitude())
            .longitude(salePostEsTestDocument.getLongitude())

            .thumbnailUrl(salePostEsTestDocument.getThumbnailUrl())

            .salePostLiked(salePostEsTestDocument.isSalePostLiked())
            .build();
    }

}
