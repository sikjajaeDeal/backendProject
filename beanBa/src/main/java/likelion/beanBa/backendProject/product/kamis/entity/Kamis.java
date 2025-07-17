package likelion.beanBa.backendProject.product.kamis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Kamis {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "market_pk")
  private Long id;

  @Column(name = "p_itemcode", length = 50, nullable = false)
  private String itemCode;

  @Column(name = "base_date", length = 10)
  private String baseDate;

  @Column(length = 255)
  private String price;

  @Column
  private LocalDateTime updatedAt;


  @Builder
  private Kamis(Long id, String itemCode, String baseDate, String price, LocalDateTime updatedAt) {
    this.id = id;
    this.itemCode = itemCode;
    this.baseDate = baseDate;
    this.price = price;
    this.updatedAt = updatedAt;
  }

  public static Kamis of(Long id, String itemCode, String baseDate, String price) {
    return Kamis.builder()
        .id(id)
        .itemCode(itemCode)
        .baseDate(baseDate)
        .price(price)
        .updatedAt(LocalDateTime.now())
        .build();
  }


}