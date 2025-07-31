package dev.al.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
//Tabela e vizitave

@Entity
@Getter
@Setter
@Table(name = "appointments" , schema = "public")
@Data
@Builder
@AllArgsConstructor
public class Appointment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime appointmentDate;

    //Lidhje Many to one midis pacientit dhe vizitave
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    //Lidhje many to one midis doktorit dhe vizitave
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    private String reason;

    private LocalDateTime appointmentTime;


    public Appointment() {}



}