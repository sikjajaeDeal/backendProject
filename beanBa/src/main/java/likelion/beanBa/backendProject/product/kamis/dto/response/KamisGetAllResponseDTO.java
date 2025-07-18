package likelion.beanBa.backendProject.product.kamis.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.SecondaryTable;
import java.time.LocalDateTime;
import likelion.beanBa.backendProject.product.kamis.entity.Kamis;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KamisGetAllResponseDTO {

  private String itemCode;

  private String itemName;

  private String baseDate;

  private String price;

  private LocalDateTime updatedAt;

  public static KamisGetAllResponseDTO from(Kamis kamis) {
    return KamisGetAllResponseDTO.builder()
        .itemCode(kamis.getItemCode())
        .itemName(kamis.getItemName())
        .baseDate(kamis.getBaseDate())
        .price(String.valueOf(kamis.getPrice()))
        .updatedAt(kamis.getUpdatedAt())
        .build();
  }


}
