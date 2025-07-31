package dev.al.service;
import dev.al.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findAll();
    Patient save(Patient patient);
    boolean delete(Long id);
    Optional<Patient> findPatientById(Long id);

}
