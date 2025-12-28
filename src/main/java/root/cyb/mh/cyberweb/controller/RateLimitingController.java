package root.cyb.mh.cyberweb.controller;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import root.cyb.mh.cyberweb.service.RateLimitingService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RateLimitingController {

    @Autowired
    private RateLimitingService rateLimitingService;

    // View for the demo
    @GetMapping("/active/login")
    public String showLoginPage() {
        return "ratelimit/login";
    }

    // VULNERABLE: No Rate Limiting
    @PostMapping("/vuln/login-limit")
    public String loginVulnerable(@RequestParam String username, Model model) {
        // ... Login Logic ...
        model.addAttribute("message", "Login Allowed. Doorman didn't even wake up. (Total attempts: ∞)");
        return "ratelimit/login";
    }

    // SECURE: Rate Limited by IP
    @PostMapping("/secure/login-limit")
    public String loginSecure(HttpServletRequest request, @RequestParam String username, Model model) {
        String ip = request.getRemoteAddr();
        Bucket bucket = rateLimitingService.resolveBucket(ip);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            // ... Login Logic ...
            model.addAttribute("message", "Bouncer: 'ID check passed. Don't cause trouble.' (Tokens left: "
                    + probe.getRemainingTokens() + ")");
            model.addAttribute("allowed", true);
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            model.addAttribute("errorSecure",
                    "⛔ BOUNCER BLOCK: Too many attempts! Come back in " + waitForRefill + " seconds.");
            model.addAttribute("blocked", true);
        }
        return "ratelimit/login";
    }
}
