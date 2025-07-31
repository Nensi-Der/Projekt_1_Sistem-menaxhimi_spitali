package dev.al.service.impl;
import dev.al.model.Patient;
import dev.al.repository.PatientRepository;
import dev.al.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    //Metode e cila nxjerr listen e gjithe pacienteve
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    //Metode e cila ruan nje llogari pacienti
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    //Metode e cila fshin nje llogari pacienti
    public boolean delete(Long id) {
        int deletedCount = patientRepository.deleteByIdCustom(id);
        return deletedCount > 0;
    }

    @Override
    //Metode e cila gjen llogarine e pacientit nga ID
    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id);
    }
}
