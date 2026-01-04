package root.cyb.mh.cyberweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class SSRFController {

    @GetMapping("/vuln/ssrf")
    public String ssrfVulnerable(Model model) {
        return "ssrf/vuln";
    }

    @PostMapping("/vuln/ssrf/scan")
    public String scanVulnerable(@RequestParam String targetUrl, Model model) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Vulnerable: Follows redirects and accesses internal protocols
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line).append("\n");
            }
            in.close();

            model.addAttribute("status", "Scan Complete! Response Code: " + conn.getResponseCode());
            model.addAttribute("response", content.toString());

        } catch (Exception e) {
            model.addAttribute("status", "Error: " + e.getMessage());
            model.addAttribute("response", "Could not reach target.");
        }
        return "ssrf/vuln";
    }

    @GetMapping("/secure/ssrf")
    public String ssrfSecure(Model model) {
        return "ssrf/secure";
    }

    @PostMapping("/secure/ssrf/scan")
    public String scanSecure(@RequestParam String targetUrl, Model model) {
        try {
            // Secure Validation
            URL url = new URL(targetUrl);
            String host = url.getHost();

            // 1. Allowlist ONLY specific domains
            if (!host.equals("example.com") && !host.equals("google.com")) {
                throw new SecurityException("Domain not allowed. Only example.com and google.com are permitted.");
            }

            // 2. Block internal IPs (Basic check, not exhaustive)
            if (host.startsWith("127.") || host.startsWith("192.168.") || host.equals("localhost")) {
                throw new SecurityException("Internal network scan detected and blocked.");
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setRequestMethod("GET");

            model.addAttribute("status", "Secure Scan Complete. Response Code: " + conn.getResponseCode());
            model.addAttribute("response", "[Body content redacted for security]");

        } catch (Exception e) {
            model.addAttribute("status", "Secure Block: " + e.getMessage());
        }
        return "ssrf/secure";
    }
}
