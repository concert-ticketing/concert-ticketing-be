package ticketing.ticketing.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ticketing.ticketing.infrastructure.security.JwtAuthenticationFilter;
import ticketing.ticketing.infrastructure.security.JwtUtil;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/public/**",
                                "/oauth2/**",
                                "/login/**",
                                "/api/users/**",
                                "/api/auth/login",
                                "/oauth/session",
                                "/api/concert/**",
                                "/api/concerts/*",
                                "/api/concerts",
                                "/api/review/*",
                                "/api/admin",
                                "/api/admin/login",
                                "/api/banners",
                                "/api/notices",
                                "/api/faqs",
                                "/login/**",
                                "/upload/banner/**",
                                "/uploads/thumbnail/**",
                                "/uploads/description/**",
                                "/upload/svg_image/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN") // JWT role 그대로 사용
                        .anyRequest().hasAuthority("USER") // JWT role 그대로 사용
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000","http://192.168.0.101:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
