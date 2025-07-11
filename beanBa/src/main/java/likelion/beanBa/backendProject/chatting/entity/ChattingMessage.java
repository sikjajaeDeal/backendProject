package likelion.beanBa.backendProject.chatting.entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@AllArgsConstructor // 모든 필드 다 파라미터로 받는 생성자 어노테이션
@NoArgsConstructor // 기본생성자 어노테이션
@Getter
@Setter
@Entity
@Table(name = "chat_message")
public class ChattingMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_pk")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_pk")
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_pk_from")
    private Member member;


    private String message;

    //엔티티가 저잘될때 자동으로 시간을 기록
    @CreationTimestamp
    @JoinColumn(name = "message_at")
    private LocalDateTime messageAt;

}
