package com.consid.application.security;

import com.consid.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig extends VaadinWebSecurity {

    private static final String PASSWORD = "{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Configuring security {}", http);
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll()
        );
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(PASSWORD)
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin, createBasicUser("daniel"), createBasicUser("laurids"), createBasicUser("markus"));
    }

    private UserDetails createBasicUser(final String username) {
        return User.builder()
                .username(username)
                // password = password with this hash, don't tell anybody :-)
                .password(PASSWORD)
                .roles("USER")
                .build();
    }
}