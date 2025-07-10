package likelion.beanBa.backendProject.chatting.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * Websocket에서 사용자의 식별자를 추출하기 위한 핸드쉐이크 핸들러 클래스
 **/
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        //Websocket이 연결이 시작될 때 클라이언트의 nickname 파라미터를 읽어서Add commentMore actions
        //그 값을 Principal(인증정보)로 사용
        String nickname = getNickname(request.getURI().getQuery());
        return new StompPrincipal(nickname);
    }

    private String getNickname(String query) {
        if (query == null || !query.contains("nickname=")) {
            return "닉네임없음";
        } else {
            return query.split("nickname=")[1];
        }
    }

}
