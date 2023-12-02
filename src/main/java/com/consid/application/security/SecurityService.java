package com.consid.application.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public UserDetails getAuthenticatedUser() {
        log.info("Getting authenticated user");
        var user = authenticationContext.getAuthenticatedUser(UserDetails.class);
        log.info("Got authenticated user {}", user);
        return user.orElseThrow(() -> new IllegalStateException("No user is authenticated"));
    }

    public void logout() {
        // TODO: Implement getting username from authenticationContext without using getAuthenticatedUser
        log.info("Logging out user");
        authenticationContext.logout();
    }
}