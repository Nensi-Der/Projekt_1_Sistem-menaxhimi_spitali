package dev.al.service.impl;
import dev.al.model.Patient;
import dev.al.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient1 = Patient.builder()
                .id(1L)
                .name("B")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("b@gmail.com")
                .phone("1234567890")
                .build();

        patient2 = Patient.builder()
                .id(2L)
                .name("C")
                .birthDate(LocalDate.of(1985, 5, 15))
                .email("c@gmail.com")
                .phone("0987654321")
                .build();
    }

    @Test
    void testFindAll() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        List<Patient> patients = patientService.findAll();

        assertNotNull(patients);
        assertEquals(2, patients.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        when(patientRepository.save(patient1)).thenReturn(patient1);

        Patient saved = patientService.save(patient1);

        assertNotNull(saved);
        assertEquals(patient1.getName(), saved.getName());
        verify(patientRepository, times(1)).save(patient1);
    }

    @Test
    void testDeleteSuccess() {
        when(patientRepository.deleteByIdCustom(patient1.getId())).thenReturn(1);

        boolean result = patientService.delete(patient1.getId());

        assertTrue(result);
        verify(patientRepository, times(1)).deleteByIdCustom(patient1.getId());
    }

    @Test
    void testDeleteFailure() {
        when(patientRepository.deleteByIdCustom(patient1.getId())).thenReturn(0);

        boolean result = patientService.delete(patient1.getId());

        assertFalse(result);
        verify(patientRepository, times(1)).deleteByIdCustom(patient1.getId());
    }

    @Test
    void testFindByIdFound() {
        when(patientRepository.findById(patient1.getId())).thenReturn(Optional.of(patient1));

        Optional<Patient> found = patientService.findPatientById(patient1.getId());

        assertTrue(found.isPresent());
        assertEquals(patient1.getName(), found.get().getName());
        verify(patientRepository, times(1)).findById(patient1.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(patientRepository.findById(patient1.getId())).thenReturn(Optional.empty());

        Optional<Patient> found = patientService.findPatientById(patient1.getId());

        assertFalse(found.isPresent());
        verify(patientRepository, times(1)).findById(patient1.getId());
    }
}