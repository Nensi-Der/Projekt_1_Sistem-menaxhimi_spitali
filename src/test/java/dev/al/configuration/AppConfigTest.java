package dev.al.configuration;
import dev.al.repository.*;
import dev.al.service.*;
import dev.al.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest {

    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        //Bejme mock repository-t per te mos varur testet nga baza e te dhenave
        PatientRepository patientRepository = Mockito.mock(PatientRepository.class);
        DoctorRepository doctorRepository = Mockito.mock(DoctorRepository.class);
        AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
        PrescriptionRepository prescriptionRepository = Mockito.mock(PrescriptionRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        appConfig = new AppConfig(patientRepository, doctorRepository, appointmentRepository, prescriptionRepository, userRepository);
    }

    @Test
    void patientServiceBean_ReturnsPatientServiceImpl() {
        PatientService patientService = appConfig.patientService();
        assertThat(patientService).isNotNull();
        assertThat(patientService).isInstanceOf(PatientServiceImpl.class);
    }

    @Test
    void doctorServiceBean_ReturnsDoctorServiceImpl() {
        DoctorService doctorService = appConfig.doctorService();
        assertThat(doctorService).isNotNull();
        assertThat(doctorService).isInstanceOf(DoctorServiceImpl.class);
    }

    @Test
    void appointmentServiceBean_ReturnsAppointmentServiceImpl() {
        AppointmentService appointmentService = appConfig.appointmentService();
        assertThat(appointmentService).isNotNull();
        assertThat(appointmentService).isInstanceOf(AppointmentServiceImpl.class);
    }

    @Test
    void prescriptionServiceBean_ReturnsPrescriptionServiceImpl() {
        PrescriptionService prescriptionService = appConfig.prescriptionService();
        assertThat(prescriptionService).isNotNull();
        assertThat(prescriptionService).isInstanceOf(PrescriptionServiceImpl.class);
    }

    @Test
    void userServiceBean_ReturnsUserServiceImpl() {
        UserService userService = appConfig.userService();
        assertThat(userService).isNotNull();
        assertThat(userService).isInstanceOf(UserServiceImpl.class);
    }

    @Test
    void adminServiceBean_ReturnsAdminServiceImpl() {
        AdminService adminService = appConfig.adminService();
        assertThat(adminService).isNotNull();
        assertThat(adminService).isInstanceOf(AdminServiceImpl.class);
    }
}