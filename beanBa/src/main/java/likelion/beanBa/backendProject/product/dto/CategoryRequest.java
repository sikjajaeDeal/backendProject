package likelion.beanBa.backendProject.product.dto;


import likelion.beanBa.backendProject.product.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private Long categoryPk; //
    private String categoryName;
    private Long parentPk; // null이면 상위 카테고리
    private int level;
    private String useYn;    // "Y" or "N"
    private String deleteYn; // "Y" or "N"


    public Category toEntity(Category parent) {
        return Category.builder()
                .categoryName(this.categoryName)
                .parent(parent)
                .level(this.level)
                .useYn(this.useYn)
                .deleteYn(this.deleteYn)
                .build();
    }

}
