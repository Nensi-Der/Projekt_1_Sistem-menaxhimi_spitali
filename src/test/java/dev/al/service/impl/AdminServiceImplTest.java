package dev.al.service.impl;
import dev.al.constants.Roles;
import dev.al.model.User;
import dev.al.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@gmail.com")
                .password("password1")
                .role(Roles.PATIENT)
                .build();

        user2 = User.builder()
                .id(2L)
                .username("user2")
                .email("user2@hospitalname.com")
                .password("password2")
                .role(Roles.DOCTOR)
                .build();
    }

    @Test
    void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = adminService.findAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindUserByIdFound() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        Optional<User> found = adminService.findUserById(user1.getId());

        assertTrue(found.isPresent());
        assertEquals(user1.getUsername(), found.get().getUsername());
        verify(userRepository, times(1)).findById(user1.getId());
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.empty());

        Optional<User> found = adminService.findUserById(user1.getId());

        assertFalse(found.isPresent());
        verify(userRepository, times(1)).findById(user1.getId());
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user1)).thenReturn(user1);

        User created = adminService.createUser(user1);

        assertNotNull(created);
        assertEquals(user1.getUsername(), created.getUsername());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testDeleteUserSuccess() {
        when(userRepository.deleteByIdCustom(user1.getId())).thenReturn(1);

        boolean deleted = adminService.deleteUser(user1.getId());

        assertTrue(deleted);
        verify(userRepository, times(1)).deleteByIdCustom(user1.getId());
    }

    @Test
    void testDeleteUserFailure() {
        when(userRepository.deleteByIdCustom(user1.getId())).thenReturn(0);

        boolean deleted = adminService.deleteUser(user1.getId());

        assertFalse(deleted);
        verify(userRepository, times(1)).deleteByIdCustom(user1.getId());
    }

    @Test
    void testAssignRoleValidRole() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        adminService.assignRole(user1.getId(), Roles.ADMIN);

        assertEquals(Roles.ADMIN, user1.getRole());
        verify(userRepository, times(1)).findById(user1.getId());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testAssignRoleInvalidRole() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                adminService.assignRole(user1.getId(), "INVALID_ROLE"));

        assertEquals("Rol i pavlefshem!", exception.getMessage());
        verify(userRepository, times(1)).findById(user1.getId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAssignRoleUserNotFound() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                adminService.assignRole(user1.getId(), Roles.ADMIN));

        assertEquals("Perdoruesi nuk u gjet", exception.getMessage());
        verify(userRepository, times(1)).findById(user1.getId());
        verify(userRepository, never()).save(any(User.class));
    }
}