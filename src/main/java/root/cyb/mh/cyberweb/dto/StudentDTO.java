package root.cyb.mh.cyberweb.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private String firstName;
    private String lastName;
    // Exclude internal IDs, GPA (if private), or notes if sensitive
}
