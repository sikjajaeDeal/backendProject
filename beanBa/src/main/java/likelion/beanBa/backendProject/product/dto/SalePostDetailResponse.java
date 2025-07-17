package likelion.beanBa.backendProject.product.dto;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


/** 판매글 단건 조회 상세 정보 보기 위한 dto - 이미지 리스트 모두 포함 **/
@Getter
@Builder
public class SalePostDetailResponse {

    private Long postPk;
    private Long sellerPk;
    private String sellerNickname;
    private String categoryName;

    private String title;
    private String content;
    private int hopePrice;
    private Long viewCount;

    @Builder.Default
    private int likeCount = 0;

    private LocalDateTime postAt;
    private LocalDateTime stateAt;
    private SaleStatement state;
//    private Yn deleteYn;

    private Double latitude;
    private Double longitude;

    private List<String> imageUrls;

    @Builder.Default
    private boolean salePostLiked = false; // 값 반환을 빠뜨렸을 경우 대비, 디폴트 설정

    public static SalePostDetailResponse from(SalePost salePost, List<String> imageUrls, boolean salePostLiked, int likeCount) {
        return SalePostDetailResponse.builder()
                .postPk(salePost.getPostPk())
                .sellerPk(salePost.getSellerPk().getMemberPk())
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
                .imageUrls(imageUrls)
                .salePostLiked(salePostLiked)
                .build();
    }
}