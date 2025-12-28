package root.cyb.mh.cyberweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MisconfigurationController {

    @GetMapping("/vuln/error")
    public String triggerErrorVulnerable() {
        // VULNERABLE: Throws exception, bubbling up to default error page (which should
        // be configured to show stacktrace)
        throw new RuntimeException("This is a sensitive internal error message!");
    }

    @GetMapping("/secure/error")
    public String triggerErrorSecure() {
        // SECURE: Throws exception, but handled by Exception Handler below
        throw new SecureException("This is a sensitive internal error message!");
    }

    @ExceptionHandler(SecureException.class)
    public ModelAndView handleSecureException(SecureException ex) {
        // Secure handling: Show generic message, log the actual error
        ModelAndView mav = new ModelAndView("error/custom");
        mav.addObject("message", "An unexpected error occurred. Please contact support.");
        // Do NOT add ex.getMessage() or stack trace to the model for user view
        return mav;
    }

    // Custom exception for secure handling demo
    public static class SecureException extends RuntimeException {
        public SecureException(String message) {
            super(message);
        }
    }
}
