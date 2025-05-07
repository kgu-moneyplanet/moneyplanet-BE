package com.money.app.config;

import com.money.app.security.AuthenticationFilter;
import com.money.app.security.AuthEntryPoint;
import com.money.app.module.user.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationFilter authenticationFilter;
    private final AuthEntryPoint authEntryPoint;

    // 1) 비밀번호 암호화기
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 2) UserDetailsService + PasswordEncoder 를 연결하는 Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);   // 사용자 조회 서비스
        provider.setPasswordEncoder(passwordEncoder());       // 비밀번호 검증기
        return provider;
    }

    // 3) AuthenticationManager 빈 (필요 시 직접 주입 가능)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
    // 4) Security 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 끄고 세션을 사용하지 않음
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // URL별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 1) 로그인 API
                        .requestMatchers(HttpMethod.POST,
                                "/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        // 회원가입
                        .requestMatchers(HttpMethod.POST, "/users/create").permitAll()
                        // 2) OpenAPI 스펙 (JSON/YAML)
                        .requestMatchers(
                                "/v3/api-docs/**",             // 전체 스펙 JSON
                                "/v3/api-docs.yaml",           // YAML 포맷
                                "/v3/api-docs/swagger-config"  // swagger 설정
                        ).permitAll()

                        // 3) Swagger UI 리소스
                        .requestMatchers(
                                "/swagger-ui.html",            // 레거시 엔드포인트
                                "/swagger-ui/**",              // UI 정적 리소스
                                "/webjars/**"                  // swagger-ui 의 자바스크립트/스타일
                        ).permitAll()

                        // 4) 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // 예외 처리 진입
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(authEntryPoint)
                )
                // 위에서 만든 Provider 등록
                .authenticationProvider(authenticationProvider())
                // JWT 인증 필터 추가
                .addFilterBefore(
                        authenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
