package likelion.beanBa.backendProject.chatting.entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor // 모든 필드 다 파라미터로 받는 생성자 어노테이션
@NoArgsConstructor // 기본생성자 어노테이션
@Getter
@Setter
@Entity
@Table(name = "chat_room")
public class ChattingRoom {

    /** 테이블 PK **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_pk")
    private long id;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_pk")
    private SalePost salePost;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_member_pk")
    private Member buyMember;
}
