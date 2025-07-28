package ticketing.ticketing.infrastructure.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Builder
public class CustomUserDetails implements UserDetails {

    private final Long id;  // JWT sub (Long 변환 값)
    private final String username;  // 보통 이메일 또는 사용자 이름
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}