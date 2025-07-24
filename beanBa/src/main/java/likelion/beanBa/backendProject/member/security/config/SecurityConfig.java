package likelion.beanBa.backendProject.member.security.config;

import likelion.beanBa.backendProject.member.jwt.JwtTokenProvider;
import likelion.beanBa.backendProject.member.security.filter.JwtAuthenticationFilter;
import likelion.beanBa.backendProject.member.security.oauth.CustomOAuth2UserService;
import likelion.beanBa.backendProject.member.security.oauth.OAuth2LoginSuccessHandler;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
    http
        .csrf(AbstractHttpConfigurer::disable);

    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .sessionManagement((session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)));

    http.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/auth/login", "/api/auth/refresh", "/api/auth/signup/**", "/oauth2/**","/login/oauth2/**").permitAll()
            .requestMatchers("/api/member/findId", "/api/member/findPassword").permitAll()
            .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
            .requestMatchers(
                "/upload",
                "/api/test-sale-post/**",  //sale-post 테스트 하느라고 잠시 넣어놨습니다.
                "/api/sale-post/all/**",
                "/api/sale-post/detail/**",
                "/api/sale-post/top-view/**"
            ).permitAll()
            .requestMatchers("/api/health/**").permitAll() //배포 헬스체크
            .requestMatchers(
                    "api/chatting/**",
                    "/*.html",
                    "/js/**", // chatting 테스트 하느라고 잠시 넣어놨습니다.
                    "/api/ws-chat",
                    "/app/**",
                    "/api/rooms/**",
                    "/api/kamis/all",
                    "/api/sale-post/elasticsearch",
                    "/api/rooms/**"

            ).permitAll().requestMatchers(
                    "/api/admin/**",
                    "/api/report/**",
                    "/admin/**",
                            "/css/**",
                            "/images/**",
                            "/favicon.ico"
                    ).permitAll()//0717 김송이 추가
            .anyRequest().authenticated()
        )
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:8081","https://beanba.store"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true); // credentials 필요 없는 경우 false

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
