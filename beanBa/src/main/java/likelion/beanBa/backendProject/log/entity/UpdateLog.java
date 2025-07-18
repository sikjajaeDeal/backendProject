package likelion.beanBa.backendProject.log.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


/**
 * 삭제 혹은 사용유무 변경 로그 테이블
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "update_log")
public class UpdateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "update_pk")
    private Long id; // pk값

    @Column(name = "table_name")
    private String tableName; // 테이블명

    @Column(name = "record_pk")
    private Long recordPk; // 삭제 혹은 사용유무 변경한 데이터의 pk값

    @Column(name = "update_kind")
    private String updateKind; // 삭제('D') 혹은 사용유무

    @Column(name = "update_state")
    private String updateState; // 삭제 혹은 사용유무 변경 후 데이터의 상태값

    @Column(name = "update_member_pk")
    private Long updateMemberPk; // 삭제 혹은 사용유무 변경한 사용자 PK값

    //엔티티가 저장될 때 자동으로 시간을 기록
    @CreationTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt; // 삭제 혹은 사용유무 변경한 시간

    @Column(name = "update_ip")
    private String updateIp; // 삭제 혹은 사용유무 변경한 사용자 IP값
}
