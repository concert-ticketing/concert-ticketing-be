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

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    public boolean isTokenValid(String token) {
        try {
            return !getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            logger.warning("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }
}
