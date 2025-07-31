package dev.al.service.impl;
import dev.al.model.Appointment;
import dev.al.model.Patient;
import dev.al.model.Prescription;
import dev.al.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    private Appointment appointment;
    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup minimal valid Appointment and Patient instances
        appointment = Appointment.builder()
                .id(1L)
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .build();

        patient = Patient.builder()
                .id(1L)
                .name("PatientName")
                .build();
    }

    private Prescription createPrescription() {
        return Prescription.builder()
                .id(1L)
                .appointment(appointment)
                .patient(patient)
                .medication("Paracetamol")
                .dosage("500mg dy here ne dite")
                .instructions("Pas vakteve")
                .build();
    }

    @Test
    void testFindAll() {
        Prescription prescription = createPrescription();
        when(prescriptionRepository.findAll()).thenReturn(List.of(prescription));

        List<Prescription> prescriptions = prescriptionService.findAll();

        assertEquals(1, prescriptions.size());
        verify(prescriptionRepository).findAll();
    }

    @Test
    void testFindById() {
        Prescription prescription = createPrescription();
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));

        Optional<Prescription> found = prescriptionService.findPrescriptionById(1L);

        assertTrue(found.isPresent());
        assertEquals("Paracetamol", found.get().getMedication());
        verify(prescriptionRepository).findById(1L);
    }

    @Test
    void testFindByAppointmentId() {
        Prescription prescription = createPrescription();
        when(prescriptionRepository.findByAppointmentId(1L)).thenReturn(List.of(prescription));

        List<Prescription> list = prescriptionService.getPrescriptionsByAppointment(1L);

        assertEquals(1, list.size());
        verify(prescriptionRepository).findByAppointmentId(1L);
    }

    @Test
    void testSave() {
        Prescription prescription = createPrescription();
        when(prescriptionRepository.save(prescription)).thenReturn(prescription);

        Prescription saved = prescriptionService.createPrescription(prescription);

        assertNotNull(saved);
        assertEquals("Paracetamol", saved.getMedication());
        verify(prescriptionRepository).save(prescription);
    }

    @Test
    void testDelete_ReturnsTrueWhenDeleted() {
        when(prescriptionRepository.deleteByIdCustom(1L)).thenReturn(1);

        boolean result = prescriptionService.delete(1L);

        assertTrue(result);
        verify(prescriptionRepository).deleteByIdCustom(1L);
    }

    @Test
    void testDelete_ReturnsFalseWhenNotDeleted() {
        when(prescriptionRepository.deleteByIdCustom(1L)).thenReturn(0);

        boolean result = prescriptionService.delete(1L);

        assertFalse(result);
        verify(prescriptionRepository).deleteByIdCustom(1L);
    }
}