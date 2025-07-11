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

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String memberId = extractMemberId(provider, attributes);
        String email = extractEmail(provider, attributes);

        Member member = memberRepository.findByEmailAndProvider(email, provider)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(email)
                            .provider(provider)
                            .memberId(memberId)
                            .role("member")
                            .build();
                    return memberRepository.save(newMember);
                });

        return new CustomUserDetails(member);
    }

    private String extractEmail(String provider, Map<String,Object> attributes) {
        if(provider.equals("google")){
            return (String) attributes.get("email");
        } else if (provider.equals("kakao")){
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            return (String) kakaoAccount.get("email");
        } else {
            throw new IllegalArgumentException("Unsupported provider: "+provider);
        }
    }

    private String extractMemberId(String provider, Map<String,Object> attributes) {
        if(provider.equals("google")){
            return (String) attributes.get("sub");
        } else if (provider.equals("kakao")){
            return String.valueOf(attributes.get("id"));
        } else {
            throw new IllegalArgumentException("Unsupported provider: "+provider);
        }
    }
}
