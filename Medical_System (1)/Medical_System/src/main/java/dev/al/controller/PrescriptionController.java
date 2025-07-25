package dev.al.controller;
import jakarta.validation.Valid;
import dev.al.model.Prescription;
import dev.al.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }
//Mapping qe tregon listen e gjithe recetave
    @GetMapping
    public List<Prescription> getAllPrescriptions() {
        return prescriptionService.findAll();
    }
//Mapping qe tregon nje recete me ane te id te vizites
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        return prescriptionService.findPrescriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping per krijimin e nje recete te re
    @PostMapping
    public Prescription createPrescription(@Valid @RequestBody Prescription prescription) {
        return prescriptionService.createPrescription(prescription);
    }
//Mapping per dryshimin e nje recete ekzistuese
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescription) {
        // Assuming PrescriptionService has an update method or use save with id
        prescription.setId(id);
        Prescription updated = prescriptionService.createPrescription(prescription);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//Mapping per te fshire e nje recete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        boolean deleted = prescriptionService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}