package com.gdsc.toplearth_server.core.security.info;

import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    @Getter private final UUID uuid;
    @Getter private final EUserRole userRole;
    private final Collection<? extends GrantedAuthority> authorities;


    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole()));
        return UserPrincipal.builder()
                .uuid(user.getId())
                .userRole(user.getUserRole())
                .authorities(authorities)
                .build();
    }


    @Override
    public String getUsername() {
        return uuid.toString();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
