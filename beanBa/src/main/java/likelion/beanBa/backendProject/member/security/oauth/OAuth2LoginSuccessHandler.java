package likelion.beanBa.backendProject.member.security.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.auth.Entity.Auth;
import likelion.beanBa.backendProject.member.auth.repository.AuthRepository;
import likelion.beanBa.backendProject.member.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        Member member = oauth2User.getMember();

        String accessToken = jwtTokenProvider.generateAccessToken(member.getMemberId());
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7*24*60*60);
        response.addCookie(refreshCookie);

        authRepository.findByMemberAndDeleteYn(member, "N")
                .ifPresentOrElse(auth -> auth.updateToken(refreshToken),
                        () -> authRepository.save(new Auth(member,refreshToken)));

        String redirectUrl = "https://beanba.store/oauth2/callback"
                +"?accessToken="+ URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
