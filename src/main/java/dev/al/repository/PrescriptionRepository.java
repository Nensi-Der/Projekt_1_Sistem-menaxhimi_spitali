package dev.al.repository;
import dev.al.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByAppointmentId(Long appointmentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Prescription p WHERE p.id = ?1")
    int deleteByIdCustom(Long id);
}