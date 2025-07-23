package likelion.beanBa.backendProject.product.elasticsearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElasticInsertRequestDTO {
  private Long categoryPk;
  private String title;
  private String content;
  private Integer hopePrice;
  private Double latitude;
  private Double longitude;
}
