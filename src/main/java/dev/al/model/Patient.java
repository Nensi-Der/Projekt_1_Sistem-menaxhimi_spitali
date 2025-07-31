package dev.al.model;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//Tabela e pacienteve
@Entity
@Getter
@Setter
@Table(name = "patients")
@Data
@Builder
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    private LocalDate birthDate;
    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    public Patient() {}

    public Patient(String name, LocalDate birthDate, String email, String phone) {
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
    }
//Lidhje many to many midis doktoreve dhe pacienteve
    @ManyToMany(mappedBy = "patients")
    private Set<Doctor> doctors = new HashSet<>();

}