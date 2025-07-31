package dev.al.service.impl;
import dev.al.model.Doctor;
import dev.al.model.Patient;
import dev.al.repository.DoctorRepository;
import dev.al.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor createDoctor() {
        return Doctor.builder()
                .id(1L)
                .name("A")
                .specialty("kardiologji")
                .email("a@hositalname.com")
                .phone("1234567890")
                .available(true)
                .busyUntil(LocalDateTime.now().minusDays(1))
                .patients(new HashSet<>())
                .build();
    }

    private Patient createPatient() {
        return Patient.builder()
                .id(1L)
                .name("Y")
                .birthDate(LocalDateTime.now().minusYears(30).toLocalDate())
                .email("y@hospitalname.com")
                .phone("0987654321")
                .doctors(new HashSet<>())
                .build();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Doctor doctor = createDoctor();
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        List<Doctor> doctors = doctorService.findAll();

        assertEquals(1, doctors.size());
        verify(doctorRepository).findAll();
    }

    @Test
    void testSave() {
        Doctor doctor = createDoctor();
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor saved = doctorService.save(doctor);

        assertNotNull(saved);
        assertEquals("A", saved.getName());
        verify(doctorRepository).save(doctor);
    }

    @Test
    void testDelete_ReturnsTrueWhenDeleted() {
        when(doctorRepository.deleteByIdCustom(1L)).thenReturn(1);

        boolean result = doctorService.delete(1L);

        assertTrue(result);
        verify(doctorRepository).deleteByIdCustom(1L);
    }

    @Test
    void testDelete_ReturnsFalseWhenNotDeleted() {
        when(doctorRepository.deleteByIdCustom(1L)).thenReturn(0);

        boolean result = doctorService.delete(1L);

        assertFalse(result);
        verify(doctorRepository).deleteByIdCustom(1L);
    }

    @Test
    void testFindDoctorById() {
        Doctor doctor = createDoctor();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> found = doctorService.findDoctorById(1L);

        assertTrue(found.isPresent());
        assertEquals("A", found.get().getName());
        verify(doctorRepository).findById(1L);
    }

    @Test
    void testFindAvailableDoctors() {
        Doctor doctor = createDoctor();
        when(doctorRepository.findByAvailableTrueOrBusyUntilBefore(any(LocalDateTime.class))).thenReturn(List.of(doctor));

        List<Doctor> availableDoctors = doctorService.findAvailableDoctors();

        assertEquals(1, availableDoctors.size());
        verify(doctorRepository).findByAvailableTrueOrBusyUntilBefore(any(LocalDateTime.class));
    }

    @Test
    void testUpdateAvailability() {
        Doctor doctor = createDoctor();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any())).thenReturn(doctor);

        doctorService.updateAvailability(1L, false, LocalDateTime.now().plusDays(1));

        verify(doctorRepository).findById(1L);
        verify(doctorRepository).save(doctor);
        assertFalse(doctor.getAvailable());
    }

    @Test
    void testAddPatient() {
        Doctor doctor = createDoctor();
        Patient patient = createPatient();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        boolean added = doctorService.addPatient(1L, 1L);

        assertTrue(added);
        assertTrue(doctor.getPatients().contains(patient));
        verify(doctorRepository).save(doctor);
    }
}