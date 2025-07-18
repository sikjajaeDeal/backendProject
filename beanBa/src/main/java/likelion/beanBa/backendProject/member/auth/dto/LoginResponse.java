package likelion.beanBa.backendProject.member.auth.dto;

import likelion.beanBa.backendProject.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private MemberResponse memberResponse;
}
