package likelion.beanBa.backendProject.product.elasticsearch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
public class SearchRequestDTO {

  //위치기반
  private double latitude;
  private double longitude;

  //가격범위
  private int minPrice;
  private int maxPrice;

  //키워드
  private String keyword;

}
