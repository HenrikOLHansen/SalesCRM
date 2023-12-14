package com.consid.application.data.service;

import com.consid.application.data.entity.Role;
import com.consid.application.data.entity.User;
import com.consid.application.data.exceptions.UserException;
import com.consid.application.data.repository.UserRepository;
import com.consid.application.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityService securityService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenRegisterUser_thenSaveUser() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("pA9*#5vB1&Xr!6Tc8@Zu%4St$7Q_3q");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerUser(user, Role.builder().name("USER").build());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void whenUpdatePasswordWithNullUser_thenThrowUserException() {
        assertThrows(UserException.class, () -> {
            userService.updatePassword(null, "newStrongPassword");
        });
    }

    @Test
    public void whenRegisterUserWithExistingEmail_thenThrowUserException() {
        User user = new User();
        user.setEmail("test@email.com");
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        assertThrows(UserException.class, () -> {
            userService.registerUser(user, Role.builder().name("USER").build());
        });
    }

    @Test
    public void whenRegisterUserWithWeakPassword_thenThrowUserException() {
        User user = new User();
        user.setEmail("new@email.com");
        user.setPassword("weak");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> {
            userService.registerUser(user, Role.builder().name("USER").build());
        });
    }

    @Test
    public void whenRegisterUserWithNoRole_thenThrowUserException() {
        User user = new User();
        user.setEmail("new@email.com");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> {
            userService.registerUser(user);
        });
    }
    @Test
    public void whenGetUsers_thenCallFindAll() {
        userService.getUsers();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void whenUpdatePasswordForAnotherUser_thenThrowUserException() {
        User user = new User();
        user.setEmail("test@email.com");
        User authenticatedUser = new User();
        authenticatedUser.setEmail("other@email.com");
        authenticatedUser.setRoles(List.of(Role.builder().name("USER").build()));
        authenticatedUser.setPassword("pA9*#5vB1&Xr!6Tc8@Zu%4St$7Q_3q");

        Collection<GrantedAuthority> authorities = authenticatedUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        when(securityService.getAuthenticatedUser()).thenReturn(new org.springframework.security.core.userdetails.User(authenticatedUser.getEmail(), authenticatedUser.getPassword(), authorities));
        when(securityService.isAdmin()).thenReturn(false);

        assertThrows(UserException.class, () -> {
            userService.updatePassword(user, "newPassword");
        });
    }

    @Test
    public void whenUpdatePasswordWithWeakNewPassword_thenThrowUserException() {
        User user = new User();
        user.setEmail("test@email.com");
        User authenticatedUser = new User();
        authenticatedUser.setEmail("test@email.com");
        authenticatedUser.setRoles(List.of(Role.builder().name("USER").build()));
        authenticatedUser.setPassword("pA9*#5vB1&Xr!6Tc8@Zu%4St$7Q_3q");

        Collection<GrantedAuthority> authorities = authenticatedUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        when(securityService.getAuthenticatedUser()).thenReturn(new org.springframework.security.core.userdetails.User(authenticatedUser.getEmail(), authenticatedUser.getPassword(), authorities));
        when(securityService.isAdmin()).thenReturn(false);

        assertThrows(UserException.class, () -> {
            userService.updatePassword(user, "weak");
        });
    }
}
