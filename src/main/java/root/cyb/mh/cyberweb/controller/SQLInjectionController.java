package root.cyb.mh.cyberweb.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import root.cyb.mh.cyberweb.model.Student;
import root.cyb.mh.cyberweb.repository.StudentRepository;

import java.util.List;

@Controller
public class SQLInjectionController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/vuln/sqli")
    public String vulnerableSearch(@RequestParam(required = false) String name, Model model) {
        if (name != null) {
            // VULNERABLE: Direct string concatenation
            String query = "SELECT * FROM students WHERE first_name = '" + name + "'";
            try {
                // Using generic result set mapping for demonstration
                List<Student> students = entityManager.createNativeQuery(query, Student.class).getResultList();
                model.addAttribute("students", students);
                model.addAttribute("query", query);
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "sqli/vuln";
    }

    @GetMapping("/secure/sqli")
    public String secureSearch(@RequestParam(required = false) String name, Model model) {
        if (name != null) {
            // SECURE: Using JPA Repository (Parameterized queries)
            List<Student> students = studentRepository.findByFirstNameContainingIgnoreCase(name);
            model.addAttribute("students", students);
        }
        return "sqli/secure";
    }
}
