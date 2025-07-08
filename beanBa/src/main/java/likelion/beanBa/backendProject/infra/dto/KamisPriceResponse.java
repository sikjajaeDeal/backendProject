
package likelion.beanBa.backendProject.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KamisPriceResponse {
    private List<Item> price;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String itemname;
        private String kindname;
        private String rank;
        private String unit;
        private String price;
        private String regday;
    }
}
