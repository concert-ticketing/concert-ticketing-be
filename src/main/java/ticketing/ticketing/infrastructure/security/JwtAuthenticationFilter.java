package ticketing.ticketing.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.isTokenValid(token)) {
                    String username = jwtUtil.extractUsername(token);
                    String role = jwtUtil.extractRole(token); // "USER" 그대로 가져옴

                    // prefix 없이 그대로 권한 설정
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                    Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Authentication set successfully for role: " + role);
                }
            } catch (Exception e) {
                log.warn("JWT 토큰 검증 실패: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                String contentType = request.getContentType();
                if (contentType != null && (
                        contentType.startsWith("multipart/form-data") ||
                                contentType.equals("application/octet-stream")
                )) {
                    response.setContentType("text/plain");
                    response.getWriter().write("Invalid or expired JWT token");
                } else {
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Invalid or expired JWT token\"}");
                }
                return;
            }
        } else {
            log.info("No valid Authorization header found");
        }

        filterChain.doFilter(request, response);
    }
}
