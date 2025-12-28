package root.cyb.mh.cyberweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private Double gpa;

    @Column(columnDefinition = "TEXT")
    private String notes; // Vulnerable to Stored XSS
}
