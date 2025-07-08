package likelion.beanBa.backendProject.member.auth.Entity;

import jakarta.persistence.*;
import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authPk;

    @ManyToOne
    @JoinColumn(name = "member_pk")
    private Member member;

    @Column(nullable = false)
    private String refreshToken;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 1)
    private String deleteYn = "N";

    public Auth(Member member, String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
        this.deleteYn = "N";
    }

    public void updateToken(String newToken) {
        this.refreshToken = newToken;
        this.createdAt = LocalDateTime.now();
    }

    public void invalidateToken() {
        this.refreshToken = "logout";
    }

    public void deleted() {
        this.deleteYn = "Y";
    }
}
