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
        // private List<String> kindCode;
        private Object kindCode;

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

        // 안전하게 List<String> 형태로 꺼내기 위한 getter
        public List<String> getKindCode() {
            if (kindCode == null) return List.of();
            if (kindCode instanceof List<?>) {
                return ((List<?>) kindCode).stream()
                        .map(String::valueOf)
                        .toList();
            } else {
                return List.of(String.valueOf(kindCode));
            }
        }
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

        private Object itemname;
        private Object kindname;
        private Object countyname;
        private Object marketname;
        private String yyyy;
        private String regday;
        private String price;

        public String getItemname() {
            return extractString(itemname);
        }

        public String getKindname() {
            return extractString(kindname);
        }

        public String getCountyname() {
            return extractString(countyname);
        }

        public String getMarketname() {
            return extractString(marketname);
        }

        private String extractString(Object value) {
            if (value == null) return "";
            if (value instanceof String) return (String) value;
            if (value instanceof List) {
                List<?> list = (List<?>) value;
                return list.isEmpty() ? "" : String.valueOf(list.get(0));
            }
            return String.valueOf(value);
        }
    }
}
