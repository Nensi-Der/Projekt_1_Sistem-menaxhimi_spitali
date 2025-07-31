package dev.al.controller;
import dev.al.model.Appointment;
import dev.al.service.AppointmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

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
    void getAllAppointments_ReturnsList() {
        List<Appointment> list = List.of(new Appointment(), new Appointment());
        when(appointmentService.findAll()).thenReturn(list);

        List<Appointment> result = appointmentController.getAllAppointments();

        assertThat(result).isEqualTo(list);
        verify(appointmentService).findAll();
    }

    @Test
    void getAppointmentById_Found() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        when(appointmentService.findById(1L)).thenReturn(Optional.of(appointment));

        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(appointment);
        verify(appointmentService).findById(1L);
    }

    @Test
    void getAppointmentById_NotFound() {
        when(appointmentService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(appointmentService).findById(1L);
    }

    @Test
    void createAppointment_ReturnsCreated() {
        Appointment appointment = new Appointment();
        when(appointmentService.save(appointment)).thenReturn(appointment);

        Appointment result = appointmentController.createAppointment(appointment);

        assertThat(result).isEqualTo(appointment);
        verify(appointmentService).save(appointment);
    }

    @Test
    void updateAppointment_Found() {
        Appointment existing = new Appointment();
        existing.setId(1L);
        Appointment update = new Appointment();

        when(appointmentService.findById(1L)).thenReturn(Optional.of(existing));
        when(appointmentService.save(update)).thenReturn(update);

        ResponseEntity<Appointment> response = appointmentController.updateAppointment(1L, update);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(update);
        verify(appointmentService).findById(1L);
        verify(appointmentService).save(update);
    }

    @Test
    void updateAppointment_NotFound() {
        when(appointmentService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Appointment> response = appointmentController.updateAppointment(1L, new Appointment());

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(appointmentService).findById(1L);
        verify(appointmentService, never()).save(any());
    }

    @Test
    void deleteAppointment_Deleted() {
        when(appointmentService.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = appointmentController.deleteAppointment(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(appointmentService).delete(1L);
    }

    @Test
    void deleteAppointment_NotFound() {
        when(appointmentService.delete(1L)).thenReturn(false);

        ResponseEntity<Void> response = appointmentController.deleteAppointment(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(appointmentService).delete(1L);
    }
}