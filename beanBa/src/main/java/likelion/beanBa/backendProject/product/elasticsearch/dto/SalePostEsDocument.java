package likelion.beanBa.backendProject.product.elasticsearch.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import likelion.beanBa.backendProject.product.entity.SalePost;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@JsonIgnoreProperties(ignoreUnknown = true) // 이게 있어야 클래스 명을 저장 안함(_class : com.example.backendprojcet.board.elasticsearch.dto; 등)
@Document(indexName = "post_sale", createIndex = false) //인덱스
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalePostEsDocument {

    @Id
    private String id; // elasticsearch 문서의 id(pk)
    private Long postPk; // sale_post 의pk

    /*private Long sellerPk; // 판매자 pk*/ // id 만 있어도 검색 충분
    private String sellerId; // 판매자 id

    private Long categoryPk; // 카테고리 pk
    private String categoryName; // 카테고리 이름


    /*private Long buyerPk;// 구매자 pk*/ //id만 있어도 괜찮을 듯
    private String buyerId; // 구매자 id


    private String title;
    private String content;
    //private Long viewCount;
    private int hopePrice;
    //private String postAt;
   // private String state;
    private String deleteYn;
   // private String stateAt;

    @GeoPointField
    private GeoPoint geoLocation; // 위도 경도는 지역별 범위 검색을 위해 GeoPoint 타입 사용


    public static SalePostEsDocument from(SalePost entity) {
        GeoPoint geoLoaction = new GeoPoint(entity.getLatitude(), entity.getLongitude());
        return SalePostEsDocument.builder()
                .id(String.valueOf(entity.getPostPk()))
                .postPk(entity.getPostPk())
               // .sellerPk(entity.getSellerPk().getMemberPk())
                .sellerId(entity.getSellerPk().getMemberId())
                .categoryPk(entity.getCategoryPk().getCategoryPk())
                .categoryName(entity.getCategoryPk().getCategoryName())
                //.buyerPk(entity.getBuyerPk().getMemberPk())
                .buyerId(entity.getBuyerPk().getMemberId())
                .title(entity.getTitle())
                .content(entity.getContent())
                //.viewCount(entity.getViewCount())
                .hopePrice(entity.getHopePrice())
                //.postAt(entity.getPostAt() != null ? entity.getPostAt().toString() : null)
               // .state(entity.getState().toString())
                .deleteYn(entity.getDeleteYn().toString())
               // .stateAt(entity.getStateAt() != null ? entity.getStateAt().toString() : null)
                .geoLocation(geoLoaction)
                .build();
    }
}
