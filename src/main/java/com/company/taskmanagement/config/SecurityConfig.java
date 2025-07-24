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
                // CSRF korumasını devre dışı bırakıyoruz. REST API'ler için bu standarttır.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Bu adreslere herkesin erişmesine izin ver.
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                        // Geriye kalan tüm diğer isteklere sadece kimliği doğrulanmış kullanıcıların erişmesine izin ver.
                        .anyRequest().authenticated()
                )
                // Oturum yönetimini STATELESS olarak ayarlıyoruz, çünkü JWT kullanıyoruz.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                // Kendi JWT filtremizi, Spring'in standart filtresinden önce çalıştırıyoruz.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // H2 Console'un bir frame içinde çalışabilmesi için bu ayar gerekli.
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}