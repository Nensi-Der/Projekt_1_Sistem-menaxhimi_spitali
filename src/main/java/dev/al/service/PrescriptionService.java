package dev.al.service;
import dev.al.model.Prescription;
import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    List<Prescription> findAll();
    Optional<Prescription> findPrescriptionById(Long id);
    Prescription createPrescription(Prescription prescription);
    boolean delete(Long id);
    List<Prescription> getPrescriptionsByAppointment(Long appointmentId);
}