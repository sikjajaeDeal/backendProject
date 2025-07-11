package likelion.beanBa.backendProject.product.product_enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Yn {
    Y("Y"), N("N");
    private final String code;
}
