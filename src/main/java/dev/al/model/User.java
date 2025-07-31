package dev.al.model;
import jakarta.persistence.*;
import lombok.*;

//Tabela e perdoruesve
@Entity
@Setter
@Getter
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;

    private Long linkedId;

    public User() {}

}