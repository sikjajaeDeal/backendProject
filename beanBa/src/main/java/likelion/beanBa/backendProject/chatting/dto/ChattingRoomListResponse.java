package likelion.beanBa.backendProject.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 채팅룸 리스트 return용 dto
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoomListResponse {

    // 채팅룸 pk
    private Long chattingRoomPk;

    // 채팅룸의 가장 최근 message
    private String message;

    // message 전송시간
    private LocalDateTime messageAt;

    // 채팅룸 대화상대 pk
    private Long chatWith;

    // 채팅룸 대화상대 이름
    private String chatWithNickname;

}
