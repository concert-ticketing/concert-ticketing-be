package ticketing.ticketing.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            if (userDetails instanceof CustomUserDetails customUser) {
                return customUser.getId();
            }
        } else if (auth != null && auth.getPrincipal() instanceof Long userId) {
            return userId;
        } else if (auth != null && auth.getPrincipal() instanceof String strId) {
            try {
                return Long.parseLong(strId); // handles case where sub is used as String userId
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        // 1. Principal이 CustomUserDetails인 경우
        if (auth.getPrincipal() instanceof CustomUserDetails customUser) {
            return customUser.getRole();
        }
        if (auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
            return auth.getAuthorities().iterator().next().getAuthority();
        }
        if (auth.getPrincipal() instanceof String roleStr) {
            return roleStr;
        }

        return null;
    }

}
