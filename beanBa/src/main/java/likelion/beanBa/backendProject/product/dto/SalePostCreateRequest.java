package likelion.beanBa.backendProject.product.dto;


import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalePostCreateRequest {

    private Long categoryPk;
    private String title;
    private String content;
    private int hopePrice;

    @Size(min = 1, max = 4, message = "이미지는 1개 이상 4개 이하로 등록해야 합니다.")
    private List<String> imageUrls;
}
