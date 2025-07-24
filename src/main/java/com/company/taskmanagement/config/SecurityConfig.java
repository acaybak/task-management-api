package com.company.taskmanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
                .csrf(AbstractHttpConfigurer::disable) // 1. CSRF'i devre dışı bırak
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 2. Auth adreslerine izni ver
                        .requestMatchers(PathRequest.toH2Console()).permitAll() // 3. H2 Console'a izni ver (en doğru yöntem)
                        .anyRequest().authenticated() // 4. Geri kalan her şey kimlik doğrulaması istesin
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 5. Oturumları durumsuz yap
                .authenticationProvider(authenticationProvider) // 6. Kendi doğrulama sağlayıcımızı ekle
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // 7. Kendi JWT filtremizi ekle
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // 8. H2 Console'un çalışması için
                );

        return http.build();
    }
}