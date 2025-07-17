package likelion.beanBa.backendProject.product.elasticsearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElasticInsertRequestDTO {
  private Long postPk;
  private String sellerId;
  private String buyerId;
  private String title;
  private String content;
  private int hopePrice;
  private Long categoryPk;
  private String deleteYn;
  private double latitude;
  private double longitude;
}
