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
//Mapping e cila aktivizon metoden getAllDoctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.findAll();
    }
//Mapping e cila aktivizon metoden getDoctorById
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.findDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping e cila aktivizon metoden createDoctor dhe ben Validate rolin
    @PostMapping
    public Doctor createDoctor(@Valid @RequestBody Doctor doctor) {
        return doctorService.save(doctor);
    }
//Mapping e cila aktivizon metoden updateDoctor
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
//Mapping e cila aktivizon metoden deleteDoctor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        boolean deleted = doctorService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//Mapping e cila aktivizon metoden getAvailableDoctors
    @GetMapping("/available")
    public List<Doctor> getAvailableDoctors() {
        return doctorService.findAvailableDoctors();
    }
//Mapping e cila aktivizon metoden updateAvailability
    @PutMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable Long id,
                                                   @RequestParam Boolean available,
                                                   @RequestParam(required = false) String busyUntil) {
        LocalDateTime busyUntilTime = busyUntil != null ? LocalDateTime.parse(busyUntil) : null;
        doctorService.updateAvailability(id, available, busyUntilTime);
        return ResponseEntity.noContent().build();
    }
//Mapping e cila aktivizon metoden addPatient
    @PostMapping("/{doctorId}/patients/{patientId}")
    public ResponseEntity<Void> addPatient(@PathVariable Long doctorId, @PathVariable Long patientId) {
        doctorService.addPatient(doctorId, patientId);
        return ResponseEntity.ok().build();
    }
//Mapping e cila aktivizon metoden removePatient
    @DeleteMapping("/{doctorId}/patients/{patientId}")
    public ResponseEntity<Void> removePatient(@PathVariable Long doctorId, @PathVariable Long patientId) {
        doctorService.removePatient(doctorId, patientId);
        return ResponseEntity.noContent().build();
    }
//Mapping e cila aktivizon metoden getPatients
    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<Set<Patient>> getPatients(@PathVariable Long doctorId) {
        Set<Patient> patients = doctorService.getPatients(doctorId);
        return ResponseEntity.ok(patients);
    }

}