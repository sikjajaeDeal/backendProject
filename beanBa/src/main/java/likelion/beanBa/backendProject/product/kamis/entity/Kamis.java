package likelion.beanBa.backendProject.product.kamis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import likelion.beanBa.backendProject.product.kamis.KamisClient;
import likelion.beanBa.backendProject.product.kamis.dto.response.KamisSearchResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "market_price")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Kamis {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "market_pk")
  private Long id;

  @Column(length = 50, nullable = false)
  private String itemName;

  @Column(name = "p_itemcode", length = 50, nullable = false)
  private String itemCode;

  @Column(name = "base_date", length = 10)
  private String baseDate;

  @Column(length = 255)
  private Integer price;

  @Column
  private LocalDateTime updatedAt;


  @Builder
  private Kamis(Long id, String itemName, String itemCode, String baseDate, String price, LocalDateTime updatedAt) {
    this.id = id;
    this.itemName = itemName;
    this.itemCode = itemCode;
    this.baseDate = baseDate;

    try {
      this.price = Integer.parseInt(price);
    } catch (Exception e) {
      this.price = 0; // 예외 발생 시 가격을 0으로 설정
    }

    this.updatedAt = updatedAt;
  }

  public static Kamis from(KamisSearchResponseDTO responseDTO) throws Exception {
    KamisClient kamisClient = new KamisClient();

    return Kamis.builder()
        .id(null)
        .itemName(kamisClient.searchKamisItemNameByitemCode(responseDTO.getCondition().get(0).getItemCode()))
        .itemCode(responseDTO.getCondition().get(0).getItemCode())
        .baseDate(responseDTO.getData().getItem().get(responseDTO.getData().getItem().size() - 1).getBaseDate())
        .price(responseDTO.getData().getItem().get(responseDTO.getData().getItem().size() - 1).getPrice())
        .updatedAt(LocalDateTime.now())
        .build();
  }


}