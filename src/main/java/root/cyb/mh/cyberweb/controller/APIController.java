package root.cyb.mh.cyberweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import root.cyb.mh.cyberweb.dto.StudentDTO;
import root.cyb.mh.cyberweb.model.Student;
import root.cyb.mh.cyberweb.repository.StudentRepository;

import java.util.Optional;

@Controller
public class APIController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/vuln/api/student/{id}")
    @ResponseBody
    public Student getStudentVulnerable(@PathVariable Long id) {
        // VULNERABLE: Returns the entire Entity, potentially including sensitive fields
        // (like internal ID, notes, etc.)
        return studentRepository.findById(id).orElse(null);
    }

    @GetMapping("/secure/api/student/{id}")
    @ResponseBody
    public StudentDTO getStudentSecure(@PathVariable Long id) {
        // SECURE: Returns a DTO with only allowed fields
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            StudentDTO dto = new StudentDTO();
            dto.setFirstName(student.getFirstName());
            dto.setLastName(student.getLastName());
            return dto;
        }
        return null; // Or 404
    }

    @GetMapping("/api/demo")
    public String showApiDemo() {
        return "api/demo";
    }
}
