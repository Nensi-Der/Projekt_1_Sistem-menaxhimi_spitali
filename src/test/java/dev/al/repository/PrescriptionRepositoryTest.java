package dev.al.repository;
import dev.al.model.Appointment;
import dev.al.model.Patient;
import dev.al.model.Prescription;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PrescriptionRepositoryTest {

    @Autowired
    private PrescriptionRepository recetaRepository;

    @Autowired
    private AppointmentRepository rezervimiRepository;

    @Autowired
    private PatientRepository pacientRepository;

    @Test
    @DisplayName("Testo gjetjen e recetave sipas ID së rezervimit")
    void testGjejRecetaSipasRezervimit() {
        Patient pacient = new Patient();
        pacient.setName("Pacienti A");
        pacientRepository.save(pacient);

        Appointment rezervim = new Appointment();
        rezervim.setAppointmentDate(LocalDateTime.now());
        rezervim.setPatient(pacient);
        rezervimiRepository.save(rezervim);

        Prescription receta = new Prescription();
        receta.setAppointment(rezervim);
        receta.setMedication("Medikamenti A");
        receta.setPatient(pacient);
        recetaRepository.save(receta);

        List<Prescription> recetaTeGjetura = recetaRepository.findByAppointmentId(rezervim.getId());
        assertThat(recetaTeGjetura).hasSize(1);
        assertThat(recetaTeGjetura.getFirst().getMedication()).isEqualTo("Medikamenti A");
    }

    @Autowired
    private EntityManager entityManager;  // Shto EntityManager

    @Test
    @DisplayName("Testo fshirjen me metodën e personalizuar deleteByIdCustom")
    @Transactional
    void testFshijByIdCustom() {
        Patient patient = new Patient();
        patient.setName("Patient");
        patient.setEmail("patient@gmail.com");
        patient = pacientRepository.save(patient);

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDateTime.now());
        appointment.setPatient(patient);
        appointment = rezervimiRepository.save(appointment);

        Prescription receta = new Prescription();
        receta.setMedication("Fshi Medikamentin");
        receta.setDosage("100mg");
        receta.setInstructions("Nje here ne dite");
        receta.setAppointment(appointment);
        receta.setPatient(patient);
        receta = recetaRepository.save(receta);

        int nrFshire = recetaRepository.deleteByIdCustom(receta.getId());

        entityManager.flush();
        entityManager.clear();

        assertThat(nrFshire).isEqualTo(1);
        assertThat(recetaRepository.findById(receta.getId())).isEmpty();
    }
}