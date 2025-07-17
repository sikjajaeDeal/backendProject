package likelion.beanBa.backendProject.chatting.handler;

import java.security.Principal;

/**
 *  Principal  <- 사용자 인증 정보 구현체
 *  사용자 고유 식별자(memberPk)를 반환
 **/
public class StompPrincipal implements Principal {

    private final Long memberPk;

    public StompPrincipal(Long memberPk) {
        this.memberPk = memberPk;
    }

    public Long getMemberPk() {
        return this.memberPk;
    }

    @Override
    public String getName() {
        return String.valueOf(memberPk);
    }
}
