package ticketing.ticketing.infrastructure.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtUtil {

    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());

    private final String secretKey;
    private final long expirationTime;

    public JwtUtil() {
        Dotenv dotenv = Dotenv.load();
        this.secretKey = dotenv.get("JWT_SECRET");
        this.expirationTime = Long.parseLong(dotenv.get("JWT_EXPIRATION"));
    }

    public String generateToken(String username, String role) {
        String fullRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        return Jwts.builder()
                .setSubject(username)
                .claim("role", fullRole)  // 일관되게 "role" 키 사용
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractRole(String token) {
        try {
            Object role = getClaims(token).get("role");  // "role" 키로 통일
            return role != null ? role.toString() : null;
        } catch (Exception e) {
            logger.warning("Role extraction failed: " + e.getMessage());
            return null;
        }
    }

    public String extractUsername(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (Exception e) {
            logger.warning("Username extraction failed: " + e.getMessage());
            return null;
        }
    }

    public Long extractUserId(String token) {
        try {
            String subject = getClaims(token).getSubject();
            return subject != null ? Long.parseLong(subject) : null;
        } catch (Exception e) {
            logger.warning("UserId extraction failed: " + e.getMessage());
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims != null && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            logger.warning("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.warning("Claims parsing failed: " + e.getMessage());
            throw e; // 상위 메서드에서 처리하도록 예외 재발생
        }
    }
}
