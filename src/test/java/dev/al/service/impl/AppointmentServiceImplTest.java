package dev.al.service.impl;
import dev.al.model.Appointment;
import dev.al.model.Doctor;
import dev.al.model.Patient;
import dev.al.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Doctor createDoctor() {
        return Doctor.builder()
                .id(1L)
                .name("E")
                .specialty("dermatolog")
                .email("d@hospitalname.com")
                .phone("1231231234")
                .available(true)
                .busyUntil(LocalDateTime.now().minusDays(1))
                .patients(new HashSet<>())
                .build();
    }

    private Patient createPatient() {
        return Patient.builder()
                .id(1L)
                .name("U")
                .birthDate(LocalDateTime.now().minusYears(25).toLocalDate())
                .email("u@gmail.com")
                .phone("3213214321")
                .doctors(new HashSet<>())
                .build();
    }

    private Appointment createAppointment() {
        return Appointment.builder()
                .id(1L)
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .appointmentTime(LocalDateTime.now().plusDays(1))
                .doctor(createDoctor())
                .patient(createPatient())
                .reason("Vizite e pergjithshme")
                .build();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Appointment appointment = createAppointment();
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        List<Appointment> appointments = appointmentService.findAll();

        assertEquals(1, appointments.size());
        verify(appointmentRepository).findAll();
    }

    @Test
    void testSave() {
        Appointment appointment = createAppointment();
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment saved = appointmentService.save(appointment);

        assertNotNull(saved);
        assertEquals("Vizite e pergjithshme", saved.getReason());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void testDelete_ReturnsTrueWhenDeleted() {
        when(appointmentRepository.deleteByIdCustom(1L)).thenReturn(1);

        boolean result = appointmentService.delete(1L);

        assertTrue(result);
        verify(appointmentRepository).deleteByIdCustom(1L);
    }

    @Test
    void testDelete_ReturnsFalseWhenNotDeleted() {
        when(appointmentRepository.deleteByIdCustom(1L)).thenReturn(0);

        boolean result = appointmentService.delete(1L);

        assertFalse(result);
        verify(appointmentRepository).deleteByIdCustom(1L);
    }

    @Test
    void testFindById() {
        Appointment appointment = createAppointment();
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Optional<Appointment> found = appointmentService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Vizite e pergjithshme", found.get().getReason());
        verify(appointmentRepository).findById(1L);
    }
}
