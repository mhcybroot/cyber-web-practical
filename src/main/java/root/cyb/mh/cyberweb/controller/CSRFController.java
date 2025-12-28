package root.cyb.mh.cyberweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CSRFController {

    // Simulating a sensitive action like changing email

    @GetMapping("/vuln/csrf/change-email")
    public String showVulnerableCSRF() {
        return "csrf/vuln";
    }

    // VULNERABLE: Accepts Post but Security Config ignores CSRF for this.
    // Also commonly vulnerable if accepting GET for state change.
    @PostMapping("/vuln/csrf/change-email")
    public String changeEmailVulnerable(@RequestParam String email, Model model) {
        model.addAttribute("message", "Email changed to " + email + " (Vulnerably - No CSRF Token Required)");
        return "csrf/vuln";
    }

    @GetMapping("/secure/csrf/change-email")
    public String showSecureCSRF() {
        return "csrf/secure";
    }

    @PostMapping("/secure/csrf/change-email")
    public String changeEmailSecure(@RequestParam String email, Model model) {
        model.addAttribute("message", "Email changed to " + email + " (Securely - CSRF Token Verified)");
        return "csrf/secure";
    }
}
