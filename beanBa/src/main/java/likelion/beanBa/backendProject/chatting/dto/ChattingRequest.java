package likelion.beanBa.backendProject.chatting.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRequest {

    // 채팅 메시지
    private String message;

    // 발신자 이름
    private String from;

    // 수신자 이름(귓속말 기능 개발 시 사용)
    private String to;

    // 채팅룸 Pk
    private Long roomPk;

    // 발신자 Pk
    private Long memberPkFrom;

    // 등록한 상품 Pk
    private Long postPk;

    // 전송 시간
    private LocalDateTime messageAt;

    public ChattingRequest(String from, String message) {
        this.from = from;
        this.message = message;
    }
}
