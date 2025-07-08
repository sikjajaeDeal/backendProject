package likelion.beanBa.backendProject.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisPriceResponse {

    private List<Condition> condition;
    private DataBlock data;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Condition {
        @JsonProperty("p_startday")
        private String startday;

        @JsonProperty("p_endday")
        private String endday;

        @JsonProperty("p_itemcategorycode")
        private String itemCategoryCode;

        @JsonProperty("p_itemcode")
        private String itemCode;

        @JsonProperty("p_kindcode")
        private List<String> kindCode;

        @JsonProperty("p_productrankcode")
        private String productRankCode;

        @JsonProperty("p_countycode")
        private List<String> countyCode;

        @JsonProperty("p_convert_kg_yn")
        private String convertKgYn;

        @JsonProperty("p_key")
        private String key;

        @JsonProperty("p_id")
        private String id;

        @JsonProperty("p_returntype")
        private String returnType;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBlock {
        @JsonProperty("error_code")
        private String errorCode;

        private List<Item> item;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String itemname;
        private String kindname;
        private String countyname;
        private String marketname;
        private String yyyy;
        private String regday;
        private String price;
    }
}
