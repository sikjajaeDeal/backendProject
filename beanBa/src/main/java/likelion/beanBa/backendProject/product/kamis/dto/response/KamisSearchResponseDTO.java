package likelion.beanBa.backendProject.product.kamis.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import likelion.beanBa.backendProject.product.kamis.entity.Kamis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisSearchResponseDTO {
  private List<Condition> condition;
  private Data data;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Condition {

    @JsonProperty("p_startday")
    private String startDay;

    @JsonProperty("p_endday")
    private String endDay;

    @JsonProperty("p_itemcategorycode")
    private String itemCategoryCode;

    @JsonProperty("p_itemcode")
    private String itemCode;

//    @JsonProperty("p_kindcode")
//    private String kindCode;

    @JsonProperty("p_productrankcode")
    private String productRankCode;

    @JsonProperty("p_countycode")
    private String countyCode;

    @JsonProperty("p_convert_kg_yn")
    private String convertKgYn;

    @JsonProperty("p_key")
    private String key;

    @JsonProperty("p_id")
    private String id;

    @JsonProperty("p_returntype")
    private String returnType;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Data {

    @JsonProperty("error_code")
    private String errorCode;
    private List<Item> item;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Item {

    @JsonProperty("itemname")
    private Object itemName;
//
//    @JsonProperty("kindname")
//    private Object kindName;
//
//    @JsonProperty("countyname")
//    private String countyName;
//
//    @JsonProperty("marketname")
//    private Object marketName;

//    @JsonProperty("yyyy")
//    private String year;

    @JsonProperty("regday")
    private String baseDate;

    private String price;

    public String getPrice() {
      return price.replaceAll(",", "");
    }

    public Integer getPriceAsInteger() {
      return Integer.parseInt(getPrice());
    }

  }


//  public static KamisSearchResponseDTO from(Kamis kamis) {
//
//    return KamisSearchResponseDTO.builder()
//        .condition(List.of(
//            Condition.builder()
//                .itemCode(kamis.getItemCode())
//                .build()
//        ))
//        .data(Data.builder()
//            .item(List.of(
//                Item.builder()
//                    .baseDate(kamis.getBaseDate())
//                    .price(kamis.getPrice().toString()) // Integer를 String으로 변환
//                    .build()
//            ))
//            .build())
//        .build();
//  }




}
