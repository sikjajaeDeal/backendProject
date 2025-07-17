package likelion.beanBa.backendProject.report.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportKind {
    BUYER("B"), // 구매자 신고
    SELLER("S"); // 판매자 신고

    private final String code;

    public static ReportKind of(String code) {
        for(ReportKind rk : values()) {
            if(rk.code.equals(code)) return rk;
        }
        throw new IllegalArgumentException("Unknown reportKind : " + code);
    }
}
