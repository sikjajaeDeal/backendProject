package likelion.beanBa.backendProject.product.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    private String categoryName;
    private Long parentPk; // null이면 상위 카테고리
    private int level;
    private String useYn;    // "Y" or "N"
    private String deleteYn; // "Y" or "N"
}
