package likelion.beanBa.backendProject.member.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_pk")
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true, length = 255)
    private String memberId;

    @Column(name = "provider", nullable = true, length = 1)
    private String provider; //ex 'R' (Local), 'G' (Google), 'K' (Kakao)

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "role", nullable = false, length = 255)
    private String role;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "delete_yn", length = 1)
    private String deleteYn;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
