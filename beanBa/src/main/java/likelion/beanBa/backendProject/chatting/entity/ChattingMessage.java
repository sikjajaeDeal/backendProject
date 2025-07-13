package likelion.beanBa.backendProject.chatting.entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat_message")
public class ChattingMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_pk")
    private long id; // pk값

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_pk") // 채팅룸 pk
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_pk_from") // 발신자 멤버 pk
    private Member member;

    @Lob //TEXT 타입으로 만들기
    private String message; // 전송한 메시지

    //엔티티가 저장될 때 자동으로 시간을 기록
    @CreationTimestamp
    @JoinColumn(name = "message_at")
    private LocalDateTime messageAt; // 전송 시간

}
