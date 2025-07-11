package likelion.beanBa.backendProject.member.security.config;

import likelion.beanBa.backendProject.member.jwt.JwtTokenProvider;
import likelion.beanBa.backendProject.member.security.filter.JwtAuthenticationFilter;
import likelion.beanBa.backendProject.member.security.oauth.CustomOAuth2UserService;
import likelion.beanBa.backendProject.member.security.oauth.OAuth2LoginSuccessHandler;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement((session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/member/signup",
                        "/elasticsearch/**",
                        "/api/auth/login",
                        "/api/auth/refresh",
                        "/api/auth/signup/**",
                        "/oauth2/**",
                        "/upload",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api/test-sale-post/**"  //sale-post 테스트 하느라고 잠시 넣어놨습니다.
                ).permitAll()
                .anyRequest().authenticated()
        )
                    .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService))
                                .successHandler(oAuth2LoginSuccessHandler));

        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        return provider;
    }
}
