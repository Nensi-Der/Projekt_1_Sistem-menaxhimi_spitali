package dev.al.service.impl;

import dev.al.model.User;
import dev.al.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // This is crucial to initialize mocks properly!
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User createUser() {
        return User.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@gmail.com")
                .password("password")
                .role("PATIENT")
                .linkedId(100L)
                .build();
    }

    @Test
    void testFindAll() {
        User user = createUser();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAll();

        assertEquals(1, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void testFindById() {
        User user = createUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> found = userService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void testSave_EncodesPassword() {
        User user = createUser();
        user.setPassword("password");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.save(user);

        assertNotEquals("password", savedUser.getPassword());
        assertTrue(savedUser.getPassword().startsWith("$2a$"));
        verify(userRepository).save(savedUser);
    }

    @Test
    void testSave_AlreadyEncodedPassword() {
        User user = createUser();
        user.setPassword("$2a$10$encodedhash");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.save(user);

        assertEquals("$2a$10$encodedhash", savedUser.getPassword());
        verify(userRepository).save(savedUser);
    }

    @Test
    void testDelete_ReturnsTrueWhenDeleted() {
        when(userRepository.deleteByIdCustom(1L)).thenReturn(1);

        boolean result = userService.delete(1L);

        assertTrue(result);
        verify(userRepository).deleteByIdCustom(1L);
    }

    @Test
    void testDelete_ReturnsFalseWhenNotDeleted() {
        when(userRepository.deleteByIdCustom(1L)).thenReturn(0);

        boolean result = userService.delete(1L);

        assertFalse(result);
        verify(userRepository).deleteByIdCustom(1L);
    }

    @Test
    void testValidateLogin_ReturnsTrueIfMatch() {
        User user = createUser();
        user.setPassword(new BCryptPasswordEncoder().encode("password"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean valid = userService.validateLogin("testuser", "password");

        assertTrue(valid);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testValidateLogin_ReturnsFalseIfNoMatch() {
        User user = createUser();
        user.setPassword(new BCryptPasswordEncoder().encode("1password"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean valid = userService.validateLogin("testuser", "wrongpassword");

        assertFalse(valid);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testLogin_ReturnsUserWhenPasswordMatches() {
        User user = createUser();
        user.setPassword(new BCryptPasswordEncoder().encode("password"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> loggedInUser = userService.login("testuser", "password");

        assertTrue(loggedInUser.isPresent());
        assertEquals("testuser", loggedInUser.get().getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testLogin_ReturnsEmptyWhenPasswordDoesNotMatch() {
        User user = createUser();
        user.setPassword(new BCryptPasswordEncoder().encode("1password"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> loggedInUser = userService.login("testuser", "wrongpassword");

        assertTrue(loggedInUser.isEmpty());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testRegisterUser_CallsSave() {
        User user = createUser();

        when(userRepository.save(any())).thenReturn(user);

        userService.registerUser(user);

        verify(userRepository).save(user);
    }
}