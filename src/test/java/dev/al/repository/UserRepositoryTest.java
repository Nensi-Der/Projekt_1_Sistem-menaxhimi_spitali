package dev.al.repository;
import dev.al.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository perdoruesRepository;

    @Test
    @DisplayName("Testo gjetjen e perdoruesit sipas username")
    void testGjejSipasUsername() {
        User perdorues = new User();
        perdorues.setUsername("user1");
        perdorues.setEmail("user1@gmail.com");
        perdorues.setPassword("pass");
        perdorues.setRole("PATIENT");
        perdoruesRepository.save(perdorues);

        Optional<User> gjetur = perdoruesRepository.findByUsername("user1");
        assertThat(gjetur).isPresent();
        assertThat(gjetur.get().getEmail()).isEqualTo("user1@gmail.com");
    }

    @Test
    @DisplayName("Testo gjetjen e perdoruesit sipas email")
    void testGjejSipasEmail() {
        User perdorues = new User();
        perdorues.setUsername("user2");
        perdorues.setEmail("user2@hospitalname.com");
        perdorues.setPassword("pass");
        perdorues.setRole("DOCTOR");
        perdoruesRepository.save(perdorues);

        Optional<User> gjetur = perdoruesRepository.findByEmail("user2@hospitalname.com");
        assertThat(gjetur).isPresent();
        assertThat(gjetur.get().getUsername()).isEqualTo("user2");
    }

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void testFshijByIdCustom() {
        User perdorues = new User();
        perdorues.setUsername("f");
        perdorues.setEmail("f@gmail.com");
        perdorues.setPassword("pass");
        perdorues.setRole("PATIENT");
        perdorues = perdoruesRepository.save(perdorues);
        perdoruesRepository.flush();

        System.out.println("Deleting user with ID: " + perdorues.getId());

        int deletedCount = perdoruesRepository.deleteByIdCustom(perdorues.getId());
        assertEquals(1, deletedCount);

        perdoruesRepository.flush();

        entityManager.clear();

        Optional<User> found = perdoruesRepository.findById(perdorues.getId());
        assertThat(found).isEmpty();
    }
}