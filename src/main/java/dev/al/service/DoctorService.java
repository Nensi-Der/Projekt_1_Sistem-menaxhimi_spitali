package dev.al.service;
import dev.al.model.Doctor;
import dev.al.model.Patient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DoctorService {
    List<Doctor> findAll();
    Doctor save(Doctor doctor);
    boolean delete(Long id);
    Optional<Doctor> findDoctorById(Long id);
    List<Doctor> findAvailableDoctors();
    void updateAvailability(Long doctorId, Boolean available, LocalDateTime busyUntil);
    boolean addPatient(Long doctorId, Long patientId);
    void removePatient(Long doctorId, Long patientId);
    Set<Patient> getPatients(Long doctorId);


}