package dev.al.repository;
import dev.al.model.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Doctor d WHERE d.id = ?1")
    int deleteByIdCustom(Long id);
    List<Doctor> findByAvailableTrueOrBusyUntilBefore(LocalDateTime time);
}
