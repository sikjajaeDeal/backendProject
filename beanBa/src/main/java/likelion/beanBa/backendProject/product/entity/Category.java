package likelion.beanBa.backendProject.product.entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.product.dto.CategoryRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_pk")
    private Long categoryPk;

    @Column(name = "category_name", nullable = false, length = 255)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_pk")
    private Category parent;

    //자식 카테고리들
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();


    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "use_yn", nullable = false, length = 1)
    private String useYn;

    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn;

    // 👉 명시적 생성자 + Builder 적용
    @Builder
    public Category(String categoryName, Category parent, int level, String useYn, String deleteYn) {
        this.categoryName = categoryName;
        this.parent = parent;
        this.level = level;
        this.useYn = useYn;
        this.deleteYn = deleteYn;
    }

    // 자식 카테고리 추가
    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }




    /**카테고리 생성 함수 **/
    public static Category create(CategoryRequest req, Category parent) {
        int level = (parent != null) ? parent.getLevel() + 1 : 0;

        return Category.builder()
                .categoryName(req.getCategoryName())
                .parent(parent)
                .level(level)
                .useYn(req.getUseYn() != null ? req.getUseYn() : "Y")
                .deleteYn(req.getDeleteYn() != null ? req.getDeleteYn() : "N")
                .build();
    }

    /**카테고리 수정 함수**/
    public void update(CategoryRequest req, Category parent) {
        if (req.getCategoryName() != null) this.categoryName = req.getCategoryName();
        if (req.getUseYn() != null) this.useYn = req.getUseYn();
        if (req.getDeleteYn() != null) this.deleteYn = req.getDeleteYn();

        this.setParent(parent);
        this.level = req.getLevel(); // 🔥 직접 입력받아 수정
    }

    /**카테고리 삭제**/
    public void delete(){
        this.deleteYn="Y";
        this.useYn = "N";
    }

}



