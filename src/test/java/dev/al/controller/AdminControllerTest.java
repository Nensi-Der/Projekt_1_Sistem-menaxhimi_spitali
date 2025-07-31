package dev.al.controller;
import dev.al.model.User;
import dev.al.service.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllUsers_ReturnsUserList() {
        List<User> users = List.of(new User(), new User());
        when(adminService.findAllUsers()).thenReturn(users);

        List<User> result = adminController.getAllUsers();

        assertThat(result).isEqualTo(users);
        verify(adminService).findAllUsers();
    }

    @Test
    void getUserById_Found() {
        User user = new User();
        user.setId(1L);
        when(adminService.findUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = adminController.getUserById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(user);
        verify(adminService).findUserById(1L);
    }

    @Test
    void getUserById_NotFound() {
        when(adminService.findUserById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = adminController.getUserById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(adminService).findUserById(1L);
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        User user = new User();
        when(adminService.createUser(user)).thenReturn(user);

        User result = adminController.createUser(user);

        assertThat(result).isEqualTo(user);
        verify(adminService).createUser(user);
    }

    @Test
    void deleteUser_Deleted() {
        when(adminService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<Void> response = adminController.deleteUser(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(adminService).deleteUser(1L);
    }

    @Test
    void deleteUser_NotFound() {
        when(adminService.deleteUser(1L)).thenReturn(false);

        ResponseEntity<Void> response = adminController.deleteUser(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(adminService).deleteUser(1L);
    }

    @Test
    void assignRole_Success() {
        doNothing().when(adminService).assignRole(1L, "ROLE_ADMIN");

        ResponseEntity<Void> response = adminController.assignRole(1L, "ROLE_ADMIN");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(adminService).assignRole(1L, "ROLE_ADMIN");
    }

    @Test
    void assignRole_BadRequest() {
        doThrow(new IllegalArgumentException()).when(adminService).assignRole(1L, "INVALID_ROLE");

        ResponseEntity<Void> response = adminController.assignRole(1L, "INVALID_ROLE");

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        verify(adminService).assignRole(1L, "INVALID_ROLE");
    }
}