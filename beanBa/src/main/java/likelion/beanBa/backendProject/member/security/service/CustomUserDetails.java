package likelion.beanBa.backendProject.member.security.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final Member member;

    private Map<String,Object> attributes;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    public CustomUserDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_"+member.getRole());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"N".equals(member.getUseYn());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !"N".equals(member.getDeleteYn());
    }

    @Override
    public Map<String,Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(member.getMemberId());
    }
}
