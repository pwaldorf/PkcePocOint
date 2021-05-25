package com.oint.poc.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    KeyclockClient keyclockClient;

    public UserDetails loadUser(String token) {

        UserDetails userDetails = new UserDetails() {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return keyclockClient.getAuthorities(token);
            }

            @Override
            public String getPassword() {
                return "";
            }

            @Override
            public String getUsername() {
                String username = JwtTokenUtil.getUsernameFromToken(token);
                return username.replace("service-account-", "");
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
        };

        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }
}

