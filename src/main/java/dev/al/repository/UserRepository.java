package dev.al.repository;
import dev.al.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = ?1")
    int deleteByIdCustom(Long id);
    Optional<User> findByEmail(String email);
}