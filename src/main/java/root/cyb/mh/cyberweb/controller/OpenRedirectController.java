package root.cyb.mh.cyberweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OpenRedirectController {

    @GetMapping("/vuln/redirect")
    public String vulnerableRedirect(@RequestParam String url) {
        // VULNERABLE: Redirects to any user-provided URL without validation
        return "redirect:" + url;
    }

    @GetMapping("/secure/redirect")
    public String secureRedirect(@RequestParam String url) {
        // SECURE: Ensure URL is relative (starts with /) and doesn't contain //
        // (protocol relative)
        if (url.startsWith("/") && !url.startsWith("//")) {
            return "redirect:" + url;
        }
        // Fallback to home if invalid
        return "redirect:/";
    }
}
