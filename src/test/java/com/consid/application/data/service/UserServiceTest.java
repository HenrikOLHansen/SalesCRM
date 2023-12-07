package com.consid.application.data.service;
import com.consid.application.data.entity.Role;
import com.consid.application.data.entity.User;
import com.consid.application.data.repository.UserRepository;
import com.consid.application.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

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
    public void whenUpdatePasswordWithNullUser_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updatePassword(null, "newStrongPassword");
        });
    }

    @Test
    public void whenRegisterUserWithExistingEmail_thenThrowIllegalArgumentException() {
        User user = new User();
        user.setEmail("test@email.com");
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user, Role.builder().name("USER").build());
        });
    }

    @Test
    public void whenRegisterUserWithWeakPassword_thenThrowIllegalArgumentException() {
        User user = new User();
        user.setEmail("new@email.com");
        user.setPassword("weak");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user, Role.builder().name("USER").build());
        });
    }

    @Test
    public void whenRegisterUserWithNoRole_thenThrowIllegalArgumentException() {
        User user = new User();
        user.setEmail("new@email.com");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });
    }
    @Test
    public void whenGetUsers_thenCallFindAll() {
        userService.getUsers();

        verify(userRepository, times(1)).findAll();
    }
}
