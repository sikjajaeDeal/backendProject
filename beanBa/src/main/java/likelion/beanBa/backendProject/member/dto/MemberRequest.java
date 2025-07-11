package likelion.beanBa.backendProject.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRequest {
    private String nickname;
    private String password;
    private Double latitude;
    private Double longitude;
}
