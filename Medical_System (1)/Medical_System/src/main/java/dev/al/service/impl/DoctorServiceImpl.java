package dev.al.service.impl;
import dev.al.model.Doctor;
import dev.al.model.Patient;
import dev.al.repository.DoctorRepository;
import dev.al.repository.PatientRepository;
import dev.al.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
@Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override//Metode qe nxjerr listen e gjithe doktoreve
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
    @Override//Metode qe ruan informacionet e nje doktori te ri
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }


    @Override//Metode qe fshin nje doktor nga sistemi
    public boolean delete(Long id) {
        int deletedCount = doctorRepository.deleteByIdCustom(id);
        return deletedCount > 0;}

    @Override//Metode qe kerkon nje doktor nga id
    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findById(id);
    }


    @Override//Metode qe kerkon doktoret e lire
    public List<Doctor> findAvailableDoctors() {
        return doctorRepository.findByAvailableTrueOrBusyUntilBefore(LocalDateTime.now());
    }

    @Override//Metode qe perditeson statusin e doktorit
    public void updateAvailability(Long doctorId, Boolean available, LocalDateTime busyUntil) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setAvailable(available);
        doctor.setBusyUntil(busyUntil);
        doctorRepository.save(doctor);
    }

    @Override//Metode e cila i shton pacientet nje doktori ne baze te statusit
    public void addPatient(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        doctor.getPatients().add(patient);
        doctorRepository.save(doctor);
    }

    @Override//Metode qe kerkon pacientet e nje doktori nepermjet id
    public Set<Patient> getPatients(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"))
                .getPatients();
    }

    @Override//Metode qe heq paciente nga nje doktor
    public void removePatient(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        doctor.getPatients().remove(patient);
        doctorRepository.save(doctor);
    }
}