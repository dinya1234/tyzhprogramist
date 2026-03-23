package ru.shop.tyzhprogramist.tyzhprogramist.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.UserRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class SecurityUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(Long id, String username, String password, boolean enabled,
                        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static SecurityUser from(User user) {
        return new SecurityUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                Boolean.TRUE.equals(user.getIsActive()),
                mapRoles(user.getRole())
        );
    }

    private static List<GrantedAuthority> mapRoles(UserRole role) {
        if (role == null) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
        List<GrantedAuthority> list = new ArrayList<>();
        switch (role) {
            case CLIENT -> list.add(new SimpleGrantedAuthority("ROLE_USER"));
            case MODERATOR -> {
                list.add(new SimpleGrantedAuthority("ROLE_USER"));
                list.add(new SimpleGrantedAuthority("ROLE_MODERATOR"));
            }
            case ADMIN -> {
                list.add(new SimpleGrantedAuthority("ROLE_USER"));
                list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }
        return list;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }
}
