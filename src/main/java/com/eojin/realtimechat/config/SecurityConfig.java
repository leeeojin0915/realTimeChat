package com.eojin.realtimechat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/*
* Spring Security 기본 설정
* 인증/인가 사용하지 않고 모든 요청 허용
* -> 추후 JWT 인증 추가 시 설정 수정 예정
* */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                // CSRF 토큰 기능 비활성화 (REST API + 비로그인 단순 POST 용도라 우선 끔)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 모든 요청을 인증 없이 허용
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
        return security.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173", "http://127.0.0.1:5173"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**",config);
        return source;
    }
}
