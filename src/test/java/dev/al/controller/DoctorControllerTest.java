package dev.al.controller;
import dev.al.model.Doctor;
import dev.al.model.Patient;
import dev.al.service.DoctorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

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
    void getAllDoctors_ReturnsList() {
        List<Doctor> list = List.of(new Doctor(), new Doctor());
        when(doctorService.findAll()).thenReturn(list);

        List<Doctor> result = doctorController.getAllDoctors();

        assertThat(result).isEqualTo(list);
        verify(doctorService).findAll();
    }

    @Test
    void getDoctorById_Found() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        when(doctorService.findDoctorById(1L)).thenReturn(Optional.of(doctor));

        ResponseEntity<Doctor> response = doctorController.getDoctorById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(doctor);
        verify(doctorService).findDoctorById(1L);
    }

    @Test
    void getDoctorById_NotFound() {
        when(doctorService.findDoctorById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Doctor> response = doctorController.getDoctorById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(doctorService).findDoctorById(1L);
    }

    @Test
    void createDoctor_ReturnsCreated() {
        Doctor doctor = new Doctor();
        when(doctorService.save(doctor)).thenReturn(doctor);

        Doctor result = doctorController.createDoctor(doctor);

        assertThat(result).isEqualTo(doctor);
        verify(doctorService).save(doctor);
    }

    @Test
    void updateDoctor_Found() {
        Doctor existing = new Doctor();
        existing.setId(1L);
        Doctor update = new Doctor();

        when(doctorService.findDoctorById(1L)).thenReturn(Optional.of(existing));
        when(doctorService.save(update)).thenReturn(update);

        ResponseEntity<Doctor> response = doctorController.updateDoctor(1L, update);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(update);
        verify(doctorService).findDoctorById(1L);
        verify(doctorService).save(update);
    }

    @Test
    void updateDoctor_NotFound() {
        when(doctorService.findDoctorById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Doctor> response = doctorController.updateDoctor(1L, new Doctor());

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(doctorService).findDoctorById(1L);
        verify(doctorService, never()).save(any());
    }

    @Test
    void deleteDoctor_Deleted() {
        when(doctorService.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = doctorController.deleteDoctor(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(doctorService).delete(1L);
    }

    @Test
    void deleteDoctor_NotFound() {
        when(doctorService.delete(1L)).thenReturn(false);

        ResponseEntity<Void> response = doctorController.deleteDoctor(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(doctorService).delete(1L);
    }

    @Test
    void getAvailableDoctors_ReturnsList() {
        List<Doctor> list = List.of(new Doctor());
        when(doctorService.findAvailableDoctors()).thenReturn(list);

        List<Doctor> result = doctorController.getAvailableDoctors();

        assertThat(result).isEqualTo(list);
        verify(doctorService).findAvailableDoctors();
    }

    @Test
    void updateAvailability_AlwaysReturnsNoContent() {
        doNothing().when(doctorService).updateAvailability(anyLong(), anyBoolean(), any());

        ResponseEntity<Void> response = doctorController.updateAvailability(1L, true, null);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(doctorService).updateAvailability(eq(1L), eq(true), isNull());
    }

    @Test
    void addPatient_ReturnsOk() {
        when(doctorService.addPatient(1L, 2L)).thenReturn(true);

        ResponseEntity<Void> response = doctorController.addPatient(1L, 2L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(doctorService).addPatient(1L, 2L);
    }

    @Test
    void removePatient_ReturnsNoContent() {
        doNothing().when(doctorService).removePatient(1L, 2L);

        ResponseEntity<Void> response = doctorController.removePatient(1L, 2L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(doctorService).removePatient(1L, 2L);
    }

    @Test
    void getPatients_ReturnsPatients() {
        Set<Patient> patients = Set.of(new Patient());
        when(doctorService.getPatients(1L)).thenReturn(patients);

        ResponseEntity<Set<Patient>> response = doctorController.getPatients(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(patients);
        verify(doctorService).getPatients(1L);
    }
}