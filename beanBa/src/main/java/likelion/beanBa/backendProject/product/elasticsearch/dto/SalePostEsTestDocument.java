package likelion.beanBa.backendProject.product.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@JsonIgnoreProperties(ignoreUnknown = true) // 이게 있어야 클래스 명을 저장 안함(_class : com.example.backendprojcet.board.elasticsearch.dto; 등)
@Document(indexName = "sale_post_test", createIndex = false) //인덱스
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SalePostEsTestDocument {

  @Id
  private String id; // elasticsearch 문서의 id(pk)

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

  private boolean salePostLiked;

  public static SalePostEsTestDocument from(Member member, SalePost salePost, SalePostRepository salePostRepository,
      SalePostLikeRepository salePostLikeRepository, SalePostImageRepository salePostImageRepository) {

    // 찜한 게시글 postPk 만 먼저 가져오기
    Set<Long> likedPostPks = member != null
        ? salePostLikeRepository.findAllByMemberPk(member).stream()
        .map(like -> like.getPostPk().getPostPk())
        .collect(Collectors.toSet())
        : Set.of();

    int likeCount = salePostLikeRepository.countByPostPk(salePost);
    boolean salePostLiked = likedPostPks.contains(salePost.getPostPk());


    List<SalePostImage> images = salePostImageRepository
        .findAllByPostPkAndDeleteYn(salePost, Yn.N);

    String thumbnailUrl = images.stream()
        .sorted(Comparator.comparing(i -> i.getImageOrder() != null ? i.getImageOrder() : Integer.MAX_VALUE))
        .map(SalePostImage::getImageUrl)
        .findFirst()
        .orElse(null);

    return SalePostEsTestDocument.builder()
        .postPk(salePost.getPostPk())
        .sellerNickname(salePost.getSellerPk().getNickname())
        .categoryName(salePost.getCategoryPk().getCategoryName())

        .title(salePost.getTitle())
        .content(salePost.getContent())
        .hopePrice(salePost.getHopePrice())
        .viewCount(salePost.getViewCount())
        .likeCount(likeCount)

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
