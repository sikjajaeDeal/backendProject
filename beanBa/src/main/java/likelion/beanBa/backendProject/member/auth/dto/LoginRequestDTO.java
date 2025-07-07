package likelion.beanBa.backendProject.member.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {

    @NotBlank
    private String memberId;

    @NotBlank
    private String password;
}
