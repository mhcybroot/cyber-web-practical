package root.cyb.mh.cyberweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import root.cyb.mh.cyberweb.model.Student;
import root.cyb.mh.cyberweb.repository.StudentRepository;

import java.util.List;

@Controller
public class XSSController {

    @Autowired
    private StudentRepository studentRepository;

    // --- Reflected XSS ---

    @GetMapping("/vuln/xss/reflected")
    public String reflectedVulnerable(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("output", query);
        // Vulnerability is in the View using th:utext
        return "xss/reflected_vuln";
    }

    @GetMapping("/secure/xss/reflected")
    public String reflectedSecure(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("output", query);
        // Security is in the View using th:text (default escaping)
        return "xss/reflected_secure";
    }

    // --- Stored XSS ---

    @GetMapping("/vuln/xss/stored")
    public String storedVulnerable(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "xss/stored_vuln";
    }

    @PostMapping("/vuln/xss/stored/add")
    public String addNoteVulnerable(@RequestParam String name, @RequestParam String notes) {
        // Vulnerable: saving raw input, and view will render with utext
        Student student = new Student();
        student.setFirstName(name);
        student.setNotes(notes);
        studentRepository.save(student);
        return "redirect:/vuln/xss/stored";
    }

    @GetMapping("/secure/xss/stored")
    public String storedSecure(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "xss/stored_secure";
    }

    @PostMapping("/secure/xss/stored/add")
    public String addNoteSecure(@RequestParam String name, @RequestParam String notes) {
        // Secure: Still saving raw input (usually fine if output encoded),
        // but View will use th:text.
        // For extra security, one might sanitize here, but output encoding is primary
        // defense.
        Student student = new Student();
        student.setFirstName(name);
        student.setNotes(notes);
        studentRepository.save(student);
        return "redirect:/secure/xss/stored";
    }
}
