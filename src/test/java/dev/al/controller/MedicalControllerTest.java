package dev.al.controller;
import dev.al.constants.Roles;
import dev.al.model.*;
import dev.al.service.*;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MedicalControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @Mock
    private PrescriptionService prescriptionService;

    @Mock
    private UserService userService;

    @Mock
    private AdminService adminService;

    @Setter
    @Getter
    private MedicalController controller;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new MedicalController(
                appointmentService,
                doctorService,
                patientService,
                prescriptionService,
                userService,
                adminService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void userServiceLogin_WhenValidCredentials_ReturnsUser() {
        User user = new User();
        user.setRole(Roles.ADMIN);

        when(userService.login("admin", "password")).thenReturn(Optional.of(user));

        Optional<User> result = userService.login("admin", "password");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getRole()).isEqualTo(Roles.ADMIN);

        verify(userService).login("admin", "password");
    }

    @Test
    void userServiceLogin_WhenInvalidCredentials_ReturnsEmpty() {
        when(userService.login("gabim", "gabim")).thenReturn(Optional.empty());

        Optional<User> result = userService.login("gabim", "gabim");

        assertThat(result.isPresent()).isFalse();

        verify(userService).login("gabim", "gabim");
    }


    @Test
    void doctorServiceFindDoctorById_ReturnsDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(5L);

        when(doctorService.findDoctorById(5L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> result = doctorService.findDoctorById(5L);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(5L);

        verify(doctorService).findDoctorById(5L);
    }

    // Ne menyre te ngjashme do te organizoheshin dhe testet per patientService, appointmentService, etj

}