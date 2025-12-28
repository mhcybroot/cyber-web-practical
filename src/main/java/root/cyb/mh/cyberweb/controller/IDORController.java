package root.cyb.mh.cyberweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import root.cyb.mh.cyberweb.model.User;
import root.cyb.mh.cyberweb.repository.UserRepository;

import java.util.Optional;

@Controller
public class IDORController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/vuln/idor/profile")
    public String vulnerableProfile(@RequestParam Long id, Model model) {
        // VULNERABLE: No check if the logged-in user matches the requested ID
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("profile", user.get());
            return "idor/profile";
        }
        return "error";
    }

    @GetMapping("/secure/idor/profile")
    public String secureProfile(@RequestParam Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        // SECURE: Check if the requested ID belongs to the logged-in user
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            if (user.get().getUsername().equals(currentUsername)) {
                model.addAttribute("profile", user.get());
                return "idor/profile";
            } else {
                model.addAttribute("error", "Access Denied: You cannot view other users' profiles.");
            }
        } else {
            model.addAttribute("error", "User not found");
        }
        return "idor/profile";
    }
}
