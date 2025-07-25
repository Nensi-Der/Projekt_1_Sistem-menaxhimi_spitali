package dev.al.controller;
import dev.al.model.Patient;
import jakarta.validation.Valid;
import dev.al.model.Doctor;
import dev.al.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
//Mapping per te treguar te gjithe listen e doktoreve ne db
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.findAll();
    }
//Mapping qe kthen nje doktor specifik me ane te id
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.findDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping qe fut ne db nje doktor te ri
    @PostMapping
    public Doctor createDoctor(@Valid @RequestBody Doctor doctor) {
        return doctorService.save(doctor);
    }
//Mapping qe ben update informacionet e doktoreve aktuale
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.findDoctorById(id)
                .map(existingDoctor -> {
                    doctor.setId(id);
                    Doctor updated = doctorService.save(doctor);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping qe fshin viziten me nje doktor te caktuar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        boolean deleted = doctorService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//Mapping qe tregon listen e doktoreve te lire momentalisht
    @GetMapping("/available")
    public List<Doctor> getAvailableDoctors() {
        return doctorService.findAvailableDoctors();
    }
//Mapping qe tregon statusin e nje doktori aktualisht(i lire apo jo)
    @PutMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable Long id,
                                                   @RequestParam Boolean available,
                                                   @RequestParam(required = false) String busyUntil) {
        LocalDateTime busyUntilTime = busyUntil != null ? LocalDateTime.parse(busyUntil) : null;
        doctorService.updateAvailability(id, available, busyUntilTime);
        return ResponseEntity.noContent().build();
    }
//Mapping qe i ben assgin nje doktori nje pacient te ri
    @PostMapping("/{doctorId}/patients/{patientId}")
    public ResponseEntity<Void> addPatient(@PathVariable Long doctorId, @PathVariable Long patientId) {
        doctorService.addPatient(doctorId, patientId);
        return ResponseEntity.ok().build();
    }
//Mapping qe ben unassign nje pacienti nga nje doktor
    @DeleteMapping("/{doctorId}/patients/{patientId}")
    public ResponseEntity<Void> removePatient(@PathVariable Long doctorId, @PathVariable Long patientId) {
        doctorService.removePatient(doctorId, patientId);
        return ResponseEntity.noContent().build();
    }
//Mapping qe tregon te gjithe pacientet e nje doktori
    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<Set<Patient>> getPatients(@PathVariable Long doctorId) {
        Set<Patient> patients = doctorService.getPatients(doctorId);
        return ResponseEntity.ok(patients);
    }

}