package root.cyb.mh.cyberweb.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class FlagService {

    private static final Map<String, Integer> FLAGS = new HashMap<>();

    static {
        FLAGS.put("flag{SQL_MASTER}", 100);
        FLAGS.put("flag{DOM_INTRUDER}", 100);
        FLAGS.put("flag{COOKIE_MONSTER}", 100);
        FLAGS.put("flag{CONSOLE_LOG_LEAK}", 50);
        FLAGS.put("flag{IDOR_WIZARD}", 100);
    }

    public boolean validateFlag(String flag) {
        return FLAGS.containsKey(flag);
    }

    public int getPoints(String flag) {
        return FLAGS.getOrDefault(flag, 0);
    }

    public String getMessage(String flag) {
        if ("flag{SQL_MASTER}".equals(flag))
            return "Database Breached! (SQLi)";
        if ("flag{DOM_INTRUDER}".equals(flag))
            return "XSS Spotted! (Reflected)";
        if ("flag{COOKIE_MONSTER}".equals(flag))
            return "Cookie Crumbled! (Storage)";
        if ("flag{CONSOLE_LOG_LEAK}".equals(flag))
            return "Console Snooper! (Info Leak)";
        if ("flag{IDOR_WIZARD}".equals(flag))
            return "IDOR Bypassed! (Access Control)";
        return "Flag Captured!";
    }
}
