package likelion.beanBa.backendProject.member.auth.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.member.auth.dto.JwtToken;
import likelion.beanBa.backendProject.member.auth.dto.LoginRequest;
import likelion.beanBa.backendProject.member.auth.dto.LoginResponse;
import likelion.beanBa.backendProject.member.auth.dto.RefreshTokenRequest;
import likelion.beanBa.backendProject.member.auth.service.AuthService;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtToken> refresh (
            @RequestBody RefreshTokenRequest request) {
        JwtToken token = authService.reissue(request);
        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @CurrentUser CustomUserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
