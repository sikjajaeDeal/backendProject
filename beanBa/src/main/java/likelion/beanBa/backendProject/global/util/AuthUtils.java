package likelion.beanBa.backendProject.global.util;

import likelion.beanBa.backendProject.global.exception.UnauthenticatedException;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;

public class AuthUtils {

    private AuthUtils() {} // 생성자 private 처리 (유틸 클래스이므로 인스턴스화 금지)

    public static Member getAuthenticatedMember(CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getMember() == null) {
            throw new UnauthenticatedException("로그인이 필요한 기능입니다.");
        }
        return userDetails.getMember();
    }
}
