package likelion.beanBa.backendProject.member.auth.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.member.auth.dto.LoginRequestDTO;
import likelion.beanBa.backendProject.member.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody @Valid LoginRequestDTO request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }
}
