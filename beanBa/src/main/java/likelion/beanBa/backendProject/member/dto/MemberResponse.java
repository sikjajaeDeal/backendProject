package likelion.beanBa.backendProject.member.dto;

import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private String memberId;
    private String email;
    private String nickname;
    private String provider;
    private String role;
    private Double latitude;
    private Double longitude;

    public static MemberResponse from(Member member){
        return new MemberResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                member.getProvider(),
                member.getRole(),
                member.getLatitude(),
                member.getLongitude()
        );
    }
}
