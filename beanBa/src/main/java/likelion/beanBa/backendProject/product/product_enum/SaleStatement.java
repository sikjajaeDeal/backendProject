package likelion.beanBa.backendProject.product.product_enum;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SaleStatement {

    S("S"),    // 판매중
    C("C"), // 판매완료
    H("H"), //판매 보류
    R("R"); //예약중

    private final String code;


}
