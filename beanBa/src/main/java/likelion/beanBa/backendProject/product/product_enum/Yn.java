package likelion.beanBa.backendProject.product.product_enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Yn {
    Y("Y"), //삭제 예정
    B("B"), //게시글 블라인드
    N("N"); //유지
    private final String code;
}
