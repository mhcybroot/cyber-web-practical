package root.cyb.mh.cyberweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.cyb.mh.cyberweb.service.FlagService;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/flags")
public class FlagController {

    @Autowired
    private FlagService flagService;

    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitFlag(@RequestBody Map<String, String> payload) {
        String flag = payload.get("flag");
        Map<String, Object> response = new HashMap<>();

        if (flagService.validateFlag(flag)) {
            response.put("valid", true);
            response.put("points", flagService.getPoints(flag));
            response.put("message", flagService.getMessage(flag));
            return ResponseEntity.ok(response);
        } else {
            response.put("valid", false);
            response.put("message", "Invalid Flag. Keep digging!");
            return ResponseEntity.ok(response);
        }
    }
}
