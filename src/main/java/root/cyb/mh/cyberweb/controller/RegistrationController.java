package root.cyb.mh.cyberweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import root.cyb.mh.cyberweb.model.User;
import root.cyb.mh.cyberweb.model.VulnerableUser;
import root.cyb.mh.cyberweb.repository.UserRepository;
import root.cyb.mh.cyberweb.repository.VulnerableUserRepository;

@Controller
public class RegistrationController {

    @Autowired
    private VulnerableUserRepository vulnerableUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Vulnerable Registration ---

    @GetMapping("/vuln/register")
    public String showVulnerableRegistration(Model model) {
        model.addAttribute("users", vulnerableUserRepository.findTop5ByOrderByIdDesc());
        return "register/vuln";
    }

    @PostMapping("/vuln/register")
    public String registerVulnerable(@RequestParam String username, @RequestParam String password, Model model) {
        // VULNERABLE: Storing password in plain text
        VulnerableUser user = new VulnerableUser();
        user.setUsername(username);
        user.setPassword(password); // No Hashing
        user.setRole("USER");

        try {
            vulnerableUserRepository.save(user);
            model.addAttribute("message", "User registered (vulnerably)!");
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
        }

        // Refresh list
        model.addAttribute("users", vulnerableUserRepository.findTop5ByOrderByIdDesc());
        return "register/vuln";
    }

    // --- Secure Registration ---

    @GetMapping("/secure/register")
    public String showSecureRegistration(Model model) {
        model.addAttribute("users", userRepository.findTop5ByOrderByIdDesc());
        return "register/secure";
    }

    @PostMapping("/secure/register")
    public String registerSecure(@RequestParam String username, @RequestParam String password, Model model) {
        // SECURE: Hashing password before storage
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // BCrypt Hashed
        user.setRole("USER");

        try {
            userRepository.save(user);
            model.addAttribute("message", "User registered (securely)!");
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
        }

        // Refresh list
        model.addAttribute("users", userRepository.findTop5ByOrderByIdDesc());
        return "register/secure";
    }
}
