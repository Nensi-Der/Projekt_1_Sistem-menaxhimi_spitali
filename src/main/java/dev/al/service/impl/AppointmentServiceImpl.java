package dev.al.service.impl;
import dev.al.model.Appointment;
import dev.al.repository.AppointmentRepository;
import dev.al.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    //Metode qe nxjerr listen e te gjithe vizitave
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    //Metode e cila gjen nje vizite nga ID
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    //Metode e cila ruan nje vizite te re
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    //Metode e cila fshin nje vizite
    public boolean delete(Long id) {
        int deletedCount = appointmentRepository.deleteByIdCustom(id);
        return deletedCount > 0;
    }

    @Override
    //Metode e cila cakton nje orar per nje vizite te re
    public void scheduleAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override
    //Metode e cila nxjerr te gjitha vizitat e nje doktori nga ID e doktorit
    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Override
    //Metode e cila nxjerr te gjitha vizitat e nje pacienti nga ID  e pacientit
    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
}