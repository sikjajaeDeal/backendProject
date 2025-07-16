package likelion.beanBa.backendProject.chatting.handler;

import java.security.Principal;

/**
 *  Principal  <- 사용자 인증 정보 구현체
 *  고유 식별자(username,email)를 반환
 **/
public class StompPrincipal implements Principal {

    private final String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
