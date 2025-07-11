package likelion.beanBa.backendProject.global.util;

public class InputValidator {

    public static void validateHopePrice(int hopePrice) {
        if (hopePrice < 0 || hopePrice > 100_000_000) {
            throw new IllegalArgumentException("희망 가격은 0원 이상 1억 원 이하만 가능합니다.");
        }
    }
}