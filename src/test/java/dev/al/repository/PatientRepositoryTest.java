package dev.al.repository;
import dev.al.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.transaction.Transactional;
@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository pacientRepository;

    @Test
    @DisplayName("Testo ruajtjen dhe gjetjen e nje pacienti")
    void testRuajDheGjej() {
        Patient pacient = new Patient();
        pacient.setName("A");
        pacient.setBirthDate(LocalDate.of(1985, 5, 15));
        pacient.setEmail("a@gmail.com");
        pacient.setPhone("123456789");
        pacientRepository.save(pacient);

        assertThat(pacientRepository.findById(pacient.getId())).isPresent();
    }

    @Autowired
    private EntityManager entityManager;  // Shto EntityManager

    @Test
    @DisplayName("Testo fshirjen me metoden e personalizuar deleteByIdCustom")
    @Transactional
    void testFshijByIdCustom() {
        Patient pacient = new Patient();
        pacient.setName("E");
        pacient = pacientRepository.save(pacient);

        int nrFshire = pacientRepository.deleteByIdCustom(pacient.getId());

        // Flush dhe clear për të pastruar persistence context
        entityManager.flush();
        entityManager.clear();
//Sigurohemi qe informacioni eshte fshire nga databaza
        assertThat(nrFshire).isEqualTo(1);
        assertThat(pacientRepository.findById(pacient.getId())).isEmpty();
    }
}