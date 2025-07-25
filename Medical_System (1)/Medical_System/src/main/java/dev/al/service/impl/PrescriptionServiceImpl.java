package dev.al.service.impl;
import dev.al.model.Prescription;
import dev.al.repository.PrescriptionRepository;
import dev.al.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
@Autowired
    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override//Metode e cila tregon listen e gjithe recetave qe ka dhene nje doktor
    public List<Prescription> findAll() {
        return prescriptionRepository.findAll();
    }

    @Override//Metode e cila tregon gjithe recetat sipas id-se se pacientit
    public Optional<Prescription> findPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Override//Metode e cila krijon recete te re
    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    @Override//Metode e cila tregon gjithe recetat e krijuara gjate nje vizite
    public List<Prescription> getPrescriptionsByAppointment(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    @Override//Metode e cila fshin nje recete
    public boolean delete(Long id) {
        int deletedCount = prescriptionRepository.deleteByIdCustom(id);
        return deletedCount > 0;
    }

}