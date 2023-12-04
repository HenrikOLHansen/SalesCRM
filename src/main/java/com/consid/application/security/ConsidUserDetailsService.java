package com.consid.application.security;

import com.consid.application.data.entity.Role;
import com.consid.application.data.repository.UserRepository;
import com.consid.application.data.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsidUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        // we are using the email address as the username
        log.info("Loading user by username: {}", username);
        var user = userRepository.findByEmailIgnoreCase(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found for email: " + username));
        return new User(user.getEmail(), user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final com.consid.application.data.entity.User user) {
        var userRoles = user.getRoles();
        log.info("User {} got user roles: {}", user.getEmail(), userRoles);
        // if the authenticated user has no roles, we add the USER role
        if (userRoles.isEmpty()) {
            userRoles.add(Role.builder()
                    .name("USER")
                    .build());
        }
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }
}
