package dev.al.configuration;
import dev.al.repository.*;
import dev.al.service.*;
import dev.al.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;

    public AppConfig(PatientRepository patientRepository,
                     DoctorRepository doctorRepository,
                     AppointmentRepository appointmentRepository,
                     PrescriptionRepository prescriptionRepository,
                     UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public PatientService patientService() {
        return new PatientServiceImpl(patientRepository);
    }

    @Bean
    public DoctorService doctorService() {
        return new DoctorServiceImpl(doctorRepository, patientRepository);
    }

    @Bean
    public AppointmentService appointmentService() {
        return new AppointmentServiceImpl(appointmentRepository);
    }

    @Bean
    public PrescriptionService prescriptionService() {
        return new PrescriptionServiceImpl(prescriptionRepository);
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public AdminService adminService() {
        return new AdminServiceImpl(userRepository);
    }
}