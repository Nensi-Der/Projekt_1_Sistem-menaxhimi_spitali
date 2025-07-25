package dev.al.repository;
import dev.al.model.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.id = :id")
    int deleteByIdCustom(Long id);
}