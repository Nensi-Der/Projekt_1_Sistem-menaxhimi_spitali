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

    @Override
    //Metode e cila nxjerr listen e gjithe doktoreve
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    //Metode e cila ruan nje llogari doktori
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    //Metode e cila fshin llogarine e nje doktori
    public boolean delete(Long id) {
        int deletedCount = doctorRepository.deleteByIdCustom(id);
        return deletedCount > 0;
    }

    @Override
    //Metode e cila gjen nje doktor nga ID
    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    //Metode e cila nxjerr listen e doktoreve te disponueshem
    public List<Doctor> findAvailableDoctors() {
        return doctorRepository.findByAvailableTrueOrBusyUntilBefore(LocalDateTime.now());
    }

    @Override
    //Metode e cila ndryshon statusin e doktorit
    public void updateAvailability(Long doctorId, Boolean available, LocalDateTime busyUntil) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doktori nuk u gjet!"));
        doctor.setAvailable(available);
        doctor.setBusyUntil(busyUntil);
        doctorRepository.save(doctor);
    }

    @Override
    //Metode e cila shton nje Pacient
    public boolean addPatient(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doktori nuk u gjet!"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Pacienti nuk u gjet!"));
        doctor.getPatients().add(patient);
        doctorRepository.save(doctor);
        return doctor.getPatients().contains(patient);
    }

    @Override
    public Set<Patient> getPatients(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doktori nuk u gjet"))
                .getPatients();
    }

    @Override
    //Metode e cila heq nje pacient nga llogaria e nje doktori
    public void removePatient(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        doctor.getPatients().remove(patient);
        doctorRepository.save(doctor);
    }
}