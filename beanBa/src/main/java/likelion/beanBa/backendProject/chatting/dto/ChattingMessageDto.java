package likelion.beanBa.backendProject.chatting.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChattingMessageDto {

    private String message;
    private String from;

    private String to; // 귓속말을 받을 사람
    private String roomId; // 방 ID


    public ChattingMessageDto(String from, String message) {
        this.from = from;
        this.message = message;
    }
}
