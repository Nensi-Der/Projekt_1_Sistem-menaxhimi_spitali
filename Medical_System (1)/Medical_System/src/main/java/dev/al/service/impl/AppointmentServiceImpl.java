package dev.al.service.impl;

import dev.al.model.Appointment;
import dev.al.repository.AppointmentRepository;
import dev.al.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override//Metoda qe paraqet te gjitha vizitat
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override//Metoda qe gjen nje vizite ne baze te id
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override//Metoda qe ruan nje vizite te re
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override//Metoda qe prish nje vizite nga sistemi
    public boolean delete(Long id) {
        int deletedCount = appointmentRepository.deleteByIdCustom(id);
        return deletedCount > 0;
    }

    @Override//Metoda qe cakton nje vizite te re
    public void scheduleAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override//Metoda qe paraqet te gjitha vizitat e nje doktori te caktuar
    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getDoctor().getId().equals(doctorId))
                .collect(Collectors.toList());
    }

    @Override//Metoda qe nxjerr te gjitha vizitat e nje pacienti te caktuar
    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }
}