package likelion.beanBa.backendProject.product.product_enum;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SaleStatement {

    SALE("S"),    // 판매중
    COMPLETE("C");// 판매완료

    private final String code;


}
