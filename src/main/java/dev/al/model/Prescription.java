package dev.al.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
//Tabela e recetave mjekesore

@Entity
@Setter
@Getter
@Builder
@Table(name = "prescriptions")
@AllArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//Lidhje many to one midis vizitave dhe recetave
    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false)
    private String medication;

    private String dosage;

    private String instructions;
//Lidhje many to one midis pacienteve dhe recetave
    @ManyToOne
    private Patient patient;

    public Prescription() {}

//Metoda qe i paraqet pacientit receten pas vizites
    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", ilaci='" + medication + '\'' +
                ", doza='" + dosage + '\'' +
                ", instruksione='" + instructions + '\'' +
                '}';
    }
}