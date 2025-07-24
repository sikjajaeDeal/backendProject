package likelion.beanBa.backendProject.chatting.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.*;

/**
 * Websocket에서 사용자의 식별자를 추출하기 위한 핸드쉐이크 핸들러 클래스
 **/
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    /*
    * Websocket이 연결이 시작될 때 클라이언트의 memberPk 파라미터를 읽어서 Add commentMore actions
    * 그 값을 Principal(인증정보)로 사용
    */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        Long memberPk = getMemberPk(request.getURI().getQuery());
//        return new StompPrincipal(nickname);
        return new StompPrincipal(memberPk);
    }

    private Long getMemberPk(String query) {
        Long memberPk = null;
        if (query != null && query.contains("memberPk=")) {
            memberPk = Long.parseLong(query.split("memberPk=")[1]);
        }

        return memberPk;
    }

}
