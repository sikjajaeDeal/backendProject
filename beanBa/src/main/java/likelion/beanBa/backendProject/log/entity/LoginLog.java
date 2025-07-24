package likelion.beanBa.backendProject.log.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 로그인 로그 테이블
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "login_log")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_log_pk")
    private Long id; // pk값

    @Column(name = "member_pk")
    private Long memberPk; // 삭제 혹은 사용유무 변경한 사용자 PK값

    //엔티티가 저장될 때 자동으로 시간을 기록
    @CreationTimestamp
    @Column(name = "login_at")
    private LocalDateTime loginAt; // 삭제 혹은 사용유무 변경한 시간

    @Column(name = "login_ip")
    private String loginIp; // 삭제 혹은 사용유무 변경한 사용자 IP값
}
