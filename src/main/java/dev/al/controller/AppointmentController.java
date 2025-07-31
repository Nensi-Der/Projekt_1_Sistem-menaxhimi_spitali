package dev.al.controller;
import jakarta.validation.Valid;
import dev.al.model.Appointment;
import dev.al.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//Mapping per te bere nje vizite
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
//Mapping e cila aktivizon metoden getAllAppointments
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.findAll();
    }
//Mapping a cila aktivizon metoden getAppointmentById
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping e cila aktivizon metoden createAppointment
    @PostMapping
    public Appointment createAppointment(@Valid @RequestBody Appointment appointment) {
        return appointmentService.save(appointment);
    }
//Mapping e cila aktivizon metoden updateAppointment
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment( @PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.findById(id)
                .map(existingAppointment -> {
                    appointment.setId(id);
                    Appointment updated = appointmentService.save(appointment);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
//Mapping e cila aktivizon metoden deleteAppointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        boolean deleted = appointmentService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}