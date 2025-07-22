package likelion.beanBa.backendProject.product.dto;


import likelion.beanBa.backendProject.product.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {
    private Long categoryPk;
    private String categoryName;
    private Long parentPk;
    private int level;
    private String useYn;
    private String deleteYn;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .categoryPk(category.getCategoryPk())
                .categoryName(category.getCategoryName())
                .parentPk(category.getParent() != null ? category.getParent().getCategoryPk() : null)
                .level(category.getLevel())
                .useYn(category.getUseYn())
                .deleteYn(category.getDeleteYn())
                .build();
    }
}
