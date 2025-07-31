package dev.al.repository;
import dev.al.model.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Patient p WHERE p.id = ?1")
    int deleteByIdCustom(Long id);
}