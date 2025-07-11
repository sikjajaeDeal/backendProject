package likelion.beanBa.backendProject.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String nickname;

    @Email
    private String email;

    @NotBlank
    private String password;

    private Double latitude;
    private Double longitude;

}
