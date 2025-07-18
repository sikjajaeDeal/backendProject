package likelion.beanBa.backendProject.chatting.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import likelion.beanBa.backendProject.chatting.entity.ChattingRoom;
import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChattingMessageResponse {

    private long messagePk; // ChattingMessage의 pk값

    private Long roomPk; // ChattingRoom의 PK값

    private Long memberPkFrom; // Member의 PK값

    private String memberNameFrom; // Member의 닉네임

    private String message; // ChattingMessage의 message

    private LocalDateTime messageAt; // ChattingMessage의 messageAt

}
