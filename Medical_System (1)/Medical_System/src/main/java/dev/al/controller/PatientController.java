package dev.al.controller;
import jakarta.validation.Valid;
import dev.al.model.Patient;
import dev.al.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
//Mapping per te aksesuar listen e pacienteve te db
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.findAll();
    }
//Mapping per te aksesuar nje pacient specifik me ane te id
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientService.findPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping per te futur nje pacient te ri ne sistem
    @PostMapping
    public Patient createPatient(@Valid @RequestBody Patient patient) {
        return patientService.save(patient);
    }
//Mapping per te bere update te profilin e nje pacienti
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.findPatientById(id)
                .map(existingPatient -> {
                    patient.setId(id);
                    Patient updated = patientService.save(patient);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping per te fshire nje vizite me nje pacient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        boolean deleted = patientService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
