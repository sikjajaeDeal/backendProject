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

  //검색 거리 기준
  private Integer distance; // 거리 기준 (단위: km)

  //가격범위
  private int minPrice;
  private int maxPrice;

  //키워드
  private String keyword;

  //카테고리
  private Long categoryPk;

  private int page; // 페이지 번호
  private int size; // 페이지 크기 (한 페이지에 몇 개의 결과를 보여줄지)



}
