package likelion.beanBa.backendProject.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SalePostRequest {

    private Long categoryId;
    private String title;
    private String content;
    private int hopePrice;
    private Double latitude;
    private Double longitude;

    // S3 업로드 후 받은 이미지 URL들
    private List<String> imageUrls;
}
