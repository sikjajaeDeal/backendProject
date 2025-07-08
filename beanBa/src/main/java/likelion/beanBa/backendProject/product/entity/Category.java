package likelion.beanBa.backendProject.product.entity;

import jakarta.persistence.*;
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
    private Long id;

    @Column(name = "category_name", nullable = false, length = 255)
    private String name;

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
    public Category(String name, Category parent, int level, String useYn, String deleteYn) {
        this.name = name;
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
}

