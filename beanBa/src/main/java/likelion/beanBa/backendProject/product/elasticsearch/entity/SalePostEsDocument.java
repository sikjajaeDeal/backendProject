package likelion.beanBa.backendProject.product.elasticsearch.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.jdi.LongType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document(indexName = "sale_post", createIndex = false)
@Builder
public class SalePostEsDocument {

    @Id
    private String id; // elasticsearch 문서의 id(pk)
    private Long post_pk; // sale_post 의pk

    private Long sellerPk; // 판매자 pk
    private String sellerId; // 판매자 id

    private Long categoryPk; // 카테고리 pk
    private String categoryName; // 카테고리 이름


    private Long buyerPk;// 구매자 pk
    private String buyerId; // 구매자 id

    private String title;
    private String content;
    private Long viewCount;
    private int hopePrice;
    private
}
