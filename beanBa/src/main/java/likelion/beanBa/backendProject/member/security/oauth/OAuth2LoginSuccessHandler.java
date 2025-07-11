package likelion.beanBa.backendProject.member.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.auth.Entity.Auth;
import likelion.beanBa.backendProject.member.auth.dto.LoginResponse;
import likelion.beanBa.backendProject.member.auth.repository.AuthRepository;
import likelion.beanBa.backendProject.member.dto.MemberResponse;
import likelion.beanBa.backendProject.member.jwt.JwtTokenProvider;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        Member member = oauth2User.getMember();

        String accessToken = jwtTokenProvider.generateAccessToken(member.getMemberId());
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        authRepository.findByMemberAndRefreshTokenNotAndDeleteYn(member, "logout", "N")
                .ifPresentOrElse(auth -> auth.updateToken(refreshToken),
                        () -> authRepository.save(new Auth(member,refreshToken)));

        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken, MemberResponse.from(member));

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
        response.sendRedirect("/oauth2/success");
    }
}
