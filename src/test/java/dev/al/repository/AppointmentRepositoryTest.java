package dev.al.repository;
import dev.al.model.Appointment;
import dev.al.model.Doctor;
import dev.al.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository rezervimiRepository;

    @Autowired
    private DoctorRepository doktorRepository;

    @Autowired
    private PatientRepository pacientRepository;

    @Test
    @DisplayName("Testo ruajtjen dhe gjetjen e një rezervimi")
    void testRuajDheGjej() {
        Patient pacient = new Patient();
        pacient.setName("A");
        pacientRepository.save(pacient);

        Doctor doktor = new Doctor();
        doktor.setName("U");
        doktor.setAvailable(true);
        doktorRepository.save(doktor);

        Appointment rezervim = new Appointment();
        rezervim.setAppointmentDate(LocalDateTime.now());
        rezervim.setPatient(pacient);
        rezervim.setDoctor(doktor);
        rezervim.setReason("Kontroll rutine");
        rezervimiRepository.save(rezervim);

        Optional<Appointment> gjetur = rezervimiRepository.findById(rezervim.getId());

        assertThat(gjetur).isPresent();
        assertThat(gjetur.get().getReason()).isEqualTo("Kontroll rutine");
        assertThat(gjetur.get().getPatient().getName()).isEqualTo("A");    // Patient name
        assertThat(gjetur.get().getDoctor().getName()).isEqualTo("U");     // Doctor name
    }
    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Testo fshirjen me metoden e personalizuar deleteByIdCustom")
    void testFshijByIdCustom() {
        // Krijo dhe ruaj nje pacient nese kerkohet
        Patient pacient = new Patient();
        pacient.setName("Test Patient");
        pacient = pacientRepository.save(pacient);

        // Krijo dhe ruaj nje vizite te re
        Appointment rezervim = new Appointment();
        rezervim.setAppointmentDate(LocalDateTime.now());
        rezervim.setPatient(pacient);
        rezervim = rezervimiRepository.save(rezervim);

        // Fshirje e vizites me custom method
        int nrFshire = rezervimiRepository.deleteByIdCustom(rezervim.getId());
        assertThat(nrFshire).isEqualTo(1);

        // Flush and clear ne menyre qe ndryshimet te jene te dukshme
        entityManager.flush();
        entityManager.clear();

      //Kontroll i fshirjes nga databaza
        Optional<Appointment> pasFshirjes = rezervimiRepository.findById(rezervim.getId());
        assertThat(pasFshirjes).isEmpty();
    }
}