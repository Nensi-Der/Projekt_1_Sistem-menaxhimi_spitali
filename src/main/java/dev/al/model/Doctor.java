package dev.al.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
//Tabela e doktoreve
@Entity
@Getter
@Setter
@Table(name = "doctors")
@Data
@Builder
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Cdo argument perfaqeson nje kolone ne tabelen "doctors" te db
    @Column(nullable = false)
    private String name;
    private String specialty;
    private String email;
    private String phone;
    private Boolean available;
    private LocalDateTime busyUntil;

    public Doctor() {
    }


    //Lidhje many to many midis doktoreve dhe pacienteve
    @ManyToMany
    //Tabele e perbashket per doktoret dhe pacientet
    @JoinTable(
            name = "doctor_patients",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private Set<Patient> patients = new HashSet<>();
}