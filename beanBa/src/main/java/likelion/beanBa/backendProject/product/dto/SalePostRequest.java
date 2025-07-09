package likelion.beanBa.backendProject.product.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalePostRequest {
    private Long categoryPk;
    private String title;
    private String content;
    private int hopePrice;
    private Double latitude;
    private Double longitude;

    //S3 업로드 후 받은 url 들
    @Size(min = 1, max = 4, message = "이미지는 1개 이상 4개 이하로 등록해야 합니다.")
    private List<String> imageUrls;
}
