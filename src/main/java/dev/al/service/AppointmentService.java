package dev.al.service;
import dev.al.model.Appointment;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    Appointment save(Appointment appointment);
    boolean delete(Long id);
    void scheduleAppointment(Appointment appointment);
    List<Appointment> getAppointmentsForDoctor(Long doctorId);
    List<Appointment> getAppointmentsForPatient(Long patientId);
}