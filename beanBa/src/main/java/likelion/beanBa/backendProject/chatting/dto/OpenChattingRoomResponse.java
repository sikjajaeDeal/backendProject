package likelion.beanBa.backendProject.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenChattingRoomResponse {

    // 상품 상세화면에서 사용 될 채팅방의 pk값
    private Long roomPk;

    // 현재 로그인 사용자 pk값
    private Long memberPk;
}
