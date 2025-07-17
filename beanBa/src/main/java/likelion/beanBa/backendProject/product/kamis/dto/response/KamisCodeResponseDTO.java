package likelion.beanBa.backendProject.product.kamis.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisCodeResponseDTO {

  @JsonProperty("error_code")
  private String errorCode;

  @JsonProperty("info")
  private List<info> info;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class info {

    @JsonProperty("itemcategorycode")
    private String itemCategoryCode;

    @JsonProperty("itemcategoryname")
    private String itemCategoryName;

    @JsonProperty("itemcode")
    private String itemCode;

    @JsonProperty("itemname")
    private String itemName;

    @JsonProperty("kindcode")
    private String kindCode;

    @JsonProperty("kindname")
    private String kindName;

    @JsonProperty("wholesale_unit")
    private Object wholesaleUnit;

    @JsonProperty("wholesale_unitsize")
    private Object wholesaleUnitSize;

    @JsonProperty("retail_unit")
    private String retailUnit;

    @JsonProperty("retail_unitsize")
    private String retailUnitSize;

    @JsonProperty("eco_unit")
    private Object ecoUnit;

    @JsonProperty("eco_unitsize")
    private Object ecoUnitSize;

    @JsonProperty("whole_productrankcode")
    private String wholeProductrankCode;

    @JsonProperty("retail_productrankcode")
    private Object retailProductrankCode;

    @JsonProperty("new_natreu_productrankcode")
    private Object newNatreuProductrankCode;
  }

}
