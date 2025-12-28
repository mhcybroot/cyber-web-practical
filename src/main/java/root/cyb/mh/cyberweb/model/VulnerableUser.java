package root.cyb.mh.cyberweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users_plaintext")
@Data
public class VulnerableUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;
}
