package likelion.beanBa.backendProject.member.dto;


import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminMemberDTO {

    private Long memberPk;

    private String memberId;

    private String provider; // 'R' (Local), 'G' (Google), 'K' (Kakao)

    private String nickname;

    private String email;

    private String password;

    private String role;

    private String useYn;

    private String deleteYn;

    private Double latitude;

    private Double longitude;


    // Member 엔티티를 AdminMemberDTO로 변환하는 정적 팩토리 메서드
    public static AdminMemberDTO from(Member member) {
        return new AdminMemberDTO(
                member.getMemberPk(),
                member.getMemberId(),
                member.getProvider(),
                member.getNickname(),
                member.getEmail(),
                member.getPassword(),
                member.getRole(),
                member.getUseYn(),
                member.getDeleteYn(),
                member.getLatitude(),
                member.getLongitude()
        );
    }
}
