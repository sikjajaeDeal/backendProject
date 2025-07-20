package likelion.beanBa.backendProject.chatting.websocket.config;


import likelion.beanBa.backendProject.chatting.handler.CustomHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Stomp 메시지 브로커 기능을 활성화하는 어노테이션
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /*
    * 클라이언트가 웹소켓에 연결할 엔드포인트를 등록
    */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/ws-chat") // '/ws-chat' path를 통해 http 요청하여 웹소켓 연결
                .setHandshakeHandler(new CustomHandshakeHandler())
                .setAllowedOriginPatterns("*");
    }

    /*
     * Stomp에서 메세지가 어디로 전달될지 규칙(경로)을 정하는 곳
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // 구독용 경로 서버 -> 클라이언트
        registry.setApplicationDestinationPrefixes("/api"); // 전송용 경로 클라이언트 -> 서버
    }

}
