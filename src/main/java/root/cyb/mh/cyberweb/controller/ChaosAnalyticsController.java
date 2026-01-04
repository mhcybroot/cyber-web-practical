package root.cyb.mh.cyberweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class ChaosAnalyticsController {

    private final Random random = new Random();

    @GetMapping("/admin/chaos-center")
    public String chaosCenter(Model model) {
        return "admin/chaos-center";
    }

    @GetMapping("/api/chaos/metrics")
    @ResponseBody
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("tablesDropped", random.nextInt(100));
        metrics.put("firewallTears", random.nextInt(50));
        metrics.put("sanityLevel", 100 - random.nextInt(20));
        metrics.put("overflowProbability", random.nextFloat() * 100);
        return metrics;
    }
}
