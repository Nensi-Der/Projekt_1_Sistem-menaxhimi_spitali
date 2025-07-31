package dev.al.controller;
import dev.al.model.Prescription;
import dev.al.service.PrescriptionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PrescriptionControllerTest {

    @Mock
    private PrescriptionService prescriptionService;

    @InjectMocks
    private PrescriptionController prescriptionController;

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
    void getAllPrescriptions_ReturnsList() {
        List<Prescription> list = List.of(new Prescription(), new Prescription());
        when(prescriptionService.findAll()).thenReturn(list);

        List<Prescription> result = prescriptionController.getAllPrescriptions();

        assertThat(result).isEqualTo(list);
        verify(prescriptionService).findAll();
    }

    @Test
    void getPrescriptionById_Found() {
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        when(prescriptionService.findPrescriptionById(1L)).thenReturn(Optional.of(prescription));

        ResponseEntity<Prescription> response = prescriptionController.getPrescriptionById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(prescription);
        verify(prescriptionService).findPrescriptionById(1L);
    }

    @Test
    void getPrescriptionById_NotFound() {
        when(prescriptionService.findPrescriptionById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Prescription> response = prescriptionController.getPrescriptionById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}