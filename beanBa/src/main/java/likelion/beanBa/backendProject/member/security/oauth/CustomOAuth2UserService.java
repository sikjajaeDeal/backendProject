package likelion.beanBa.backendProject.member.security.oauth;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 소셜 로그인 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String provider = null;
        String providerId = null;
        String email = null;
        String nickname = null;

        Map<String,Object> attrs = oAuth2User.getAttributes();

        if("google".equals(registrationId)) {
            providerId = String.valueOf(attrs.get("sub"));
            email = String.valueOf(attrs.get("email"));
            nickname = String.valueOf(attrs.get("name"));
            provider = "G";
        } else if ("kakao".equals(registrationId)) {
            provider = "K";
            providerId = String.valueOf(attrs.get("id"));

            @SuppressWarnings("unchecked")
            Map<String,Object> kakaoAccount = (Map<String,Object>) attrs.get("kakao_account");
            if (kakaoAccount != null) {
                email = String.valueOf(kakaoAccount.get("email"));

                @SuppressWarnings("unchecked")
                Map<String,Object> profile = (Map<String,Object>) kakaoAccount.get("profile");
                if(profile != null) {
                    nickname = String.valueOf(profile.get("nickname"));
                }
            }
        }

        if(email == null || providerId == null) {
            throw new OAuth2AuthenticationException("소셜 로그인 실패 : 필수 정보 누락");
        }

        final String finalProvider = provider;
        final String finalProviderId = providerId;
        final String finalEmail = email;
        final String finalNickname = nickname;

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(finalEmail)
                                .provider(finalProvider)
                                .memberId(finalProviderId)
                                .password("temp")
                                .nickname(finalNickname != null ? finalNickname : "temp")
                                .role("member")
                                .useYn("Y")
                                .deleteYn("N")
                                .build()
                ));

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }
}
