package dev.al.repository;
import dev.al.model.Doctor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
@DataJpaTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doktorRepository;

    @Test
    @DisplayName("Testo gjetjen e doktoreve te disponueshem ose qe ka mbaruar punen")
    void testGjejDisponuesDheLiruar() {
        Doctor doktorDisponues = new Doctor();
        doktorDisponues.setName("Doktor i disponueshem");
        doktorDisponues.setAvailable(true);
        doktorRepository.save(doktorDisponues);

        Doctor doktorLiruar = new Doctor();
        doktorLiruar.setName("Doktor i lire");
        doktorLiruar.setAvailable(false);
        doktorLiruar.setBusyUntil(LocalDateTime.now().minusHours(1));
        doktorRepository.save(doktorLiruar);

        Doctor doktorIzene = new Doctor();
        doktorIzene.setName("Doktor I zene");
        doktorIzene.setAvailable(false);
        doktorIzene.setBusyUntil(LocalDateTime.now().plusHours(5));
        doktorRepository.save(doktorIzene);

        List<Doctor> doktoret = doktorRepository.findByAvailableTrueOrBusyUntilBefore(LocalDateTime.now());

        assertThat(doktoret).extracting("name")
                .containsExactlyInAnyOrder("Doktor i disponueshem", "Doktor i lire");
        assertThat(doktoret).doesNotContain(doktorIzene);
    }

    @Autowired
    private EntityManager entityManager;  // Inject EntityManager

    @Test
    @DisplayName("Testo fshirjen me metoden e personalizuar deleteByIdCustom")
    @Transactional
    void testFshijByIdCustom() {
        Doctor doktor = new Doctor();
        doktor.setName("Doktor");
        doktor = doktorRepository.save(doktor);

        int nrFshire = doktorRepository.deleteByIdCustom(doktor.getId());

        entityManager.flush();
        entityManager.clear();

        assertThat(nrFshire).isEqualTo(1);
        assertThat(doktorRepository.findById(doktor.getId())).isEmpty();
    }
}