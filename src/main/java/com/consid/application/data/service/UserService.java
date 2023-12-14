package com.consid.application.data.service;

import com.consid.application.data.entity.Role;
import com.consid.application.data.entity.User;
import com.consid.application.data.exceptions.UserException;
import com.consid.application.data.repository.UserRepository;
import com.consid.application.security.SecurityService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final static int MINIMUM_ENTROPY = 65;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public User registerUser(final User user, final Role... roles) throws UserException {
        log.info("Registering user {}", user);
        if (user == null) {
            throw new UserException("Cannot register null user");
        }
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new UserException("User with email " + user.getEmail() + " already exists");
        }
        var rolesList = List.of(roles);
        if (rolesList.isEmpty()) {
            throw new UserException("User must have at least one role");
        }
        if (calculateEntropy(user.getPassword()) < MINIMUM_ENTROPY) {
            throw new UserException("Password is too weak");
        }
        user.setRoles(rolesList);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(final String email) {
        log.info("Getting user by email {}", email);
        return userRepository.findByEmailIgnoreCase(email);
    }

    public User updatePassword(final User user, final String newPassword) throws UserException {
        log.info("Updating password for user {}, initiated by {}", user, securityService.getAuthenticatedUser());
        if (user == null) {
            throw new UserException("Cannot update password for null user");
        }
        // check that the user is the same as the authenticated user or is an admin.
        if (!user.getEmail().equals(securityService.getAuthenticatedUser().getUsername())
                || !securityService.isAdmin()) {
            throw new UserException("User is not allowed to update password for user " + user);
        }

        if (calculateEntropy(newPassword) < MINIMUM_ENTROPY) {
            throw new UserException("Password is too weak");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    /**
     * calculate the entropy of a password
     * to determined if the password is strong enough
     * 65 and above is considered strong
     * @param password
     * @return
     */
    @NotNull
    private static double calculateEntropy(final String password) {
        int charsetSize = getCharsetSize(password);
        return password.length() * (Math.log(charsetSize) / Math.log(2));
    }

    /**
     * get the charset size of a password
     * this is used to calculate the entropy.
     * @param password
     * @return
     */

    @NotNull
    private static int getCharsetSize(final String password) {
        int charset = 0;

        if (password.matches(".*[a-z].*")) charset += 26; // lowercase
        if (password.matches(".*[A-Z].*")) charset += 26; // uppercase
        if (password.matches(".*[0-9].*")) charset += 10; // numbers
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) charset += 32; // common special characters

        return charset;
    }
}
