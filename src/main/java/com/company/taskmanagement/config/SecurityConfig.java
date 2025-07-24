package com.company.taskmanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // '/api/auth/' ile başlayan tüm adreslere ve H2 konsoluna herkesin erişmesine izin ver.
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                        // Geriye kalan tüm diğer isteklere sadece kimliği doğrulanmış (giriş yapmış)
                        // kullanıcıların erişmesine izin ver.
                        .anyRequest().authenticated()
                )
                // OTURUM YÖNETİMİ: Her isteği durumsuz (stateless) yap.
                // Spring Security, session cookie'leri oluşturmayacak. Her istek JWT ile doğrulanacak.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                // OLUŞTURDUĞUMUZ FİLTREYİ, standart kullanıcı adı/şifre filtresinden ÖNCE çalışacak şekilde ekliyoruz.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // H2 Console'un frame içinde çalışabilmesi için bu ayar gerekli.
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}