package dev.al.controller;
import dev.al.model.Patient;
import dev.al.service.PatientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

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
    void getAllPatients_ReturnsList() {
        List<Patient> list = List.of(new Patient(), new Patient());
        when(patientService.findAll()).thenReturn(list);

        List<Patient> result = patientController.getAllPatients();

        assertThat(result).isEqualTo(list);
        verify(patientService).findAll();
    }

    @Test
    void getPatientById_Found() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientService.findPatientById(1L)).thenReturn(Optional.of(patient));

        ResponseEntity<Patient> response = patientController.getPatientById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(patient);
        verify(patientService).findPatientById(1L);
    }

    @Test
    void getPatientById_NotFound() {
        when(patientService.findPatientById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Patient> response = patientController.getPatientById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(patientService).findPatientById(1L);
    }

    @Test
    void createPatient_ReturnsCreated() {
        Patient patient = new Patient();
        when(patientService.save(patient)).thenReturn(patient);

        Patient result = patientController.createPatient(patient);

        assertThat(result).isEqualTo(patient);
        verify(patientService).save(patient);
    }

    @Test
    void updatePatient_Found() {
        Patient existing = new Patient();
        existing.setId(1L);
        Patient update = new Patient();

        when(patientService.findPatientById(1L)).thenReturn(Optional.of(existing));
        when(patientService.save(update)).thenReturn(update);

        ResponseEntity<Patient> response = patientController.updatePatient(1L, update);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(update);
        verify(patientService).findPatientById(1L);
        verify(patientService).save(update);
    }

    @Test
    void updatePatient_NotFound() {
        when(patientService.findPatientById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Patient> response = patientController.updatePatient(1L, new Patient());

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(patientService).findPatientById(1L);
        verify(patientService, never()).save(any());
    }

    @Test
    void deletePatient_Deleted() {
        when(patientService.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(patientService).delete(1L);
    }

    @Test
    void deletePatient_NotFound() {
        when(patientService.delete(1L)).thenReturn(false);

        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(patientService).delete(1L);
    }
}