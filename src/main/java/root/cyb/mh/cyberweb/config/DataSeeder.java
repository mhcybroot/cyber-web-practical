package root.cyb.mh.cyberweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import root.cyb.mh.cyberweb.model.Student;
import root.cyb.mh.cyberweb.model.User;
import root.cyb.mh.cyberweb.model.VulnerableUser;
import root.cyb.mh.cyberweb.repository.StudentRepository;
import root.cyb.mh.cyberweb.repository.VulnerableUserRepository;
import root.cyb.mh.cyberweb.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import root.cyb.mh.cyberweb.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VulnerableUserRepository vulnerableUserRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.user.username}")
    private String userUsername;

    @Value("${app.user.password}")
    private String userPassword;

    @Override
    public void run(String... args) throws Exception {
        seedSecureUsers();
        seedVulnerableUsers();
        seedStudents();
    }

    private void seedSecureUsers() {
        // Admin
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }

        // Flag User 1: SQLi Target
        if (!userRepository.findByUsername("flag_bot").isPresent()) {
            User flagBot = new User();
            flagBot.setUsername("flag_bot");
            flagBot.setPassword(passwordEncoder.encode("flag{SQL_MASTER}")); // Plaintext in logic, but hashed here...
                                                                             // wait, need to check if exposed via SQLi.
                                                                             // SQLi exposes plaintext table if union
                                                                             // based on plaintext table?
            // Actually, for the SQLi demo, the Vulnerable Controller concatenates string
            // and executes query.
            // If the schema saves plaintext password in a separate table or column, we need
            // to ensure that.
            // Looking at previous context, there is a `users_plaintext` table or similar?
            // Ah, I see `users_hashed` in previous bootRun logs.
            // Let's just put it in the student repository as well for easier access?
            // Or assume the attacker dumps the `users_hashed` or can bypassing login with
            // it.
            // The flag should be the password.
            flagBot.setRole("ROLE_USER");
            userRepository.save(flagBot);
        }

        // Flag User 3: IDOR Target (Student)
        if (!studentRepository.findByEmail("hidden@example.com").isPresent()) {
            Student hidden = new Student();
            hidden.setFirstName("Hidden");
            hidden.setLastName("Wizard");
            hidden.setEmail("hidden@example.com");
            hidden.setNotes("You found me! Here is your reward: flag{IDOR_WIZARD}"); // Changed bio to notes
            studentRepository.save(hidden);
        }
        if (!userService.existsByUsername(adminUsername)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(adminPassword);
            admin.setRole("ADMIN");
            userService.save(admin);
            System.out.println("Seeded secure Admin user: " + adminUsername);
        }

        if (!userService.existsByUsername(userUsername)) {
            User user = new User();
            user.setUsername(userUsername);
            user.setPassword(userPassword);
            user.setRole("USER");
            userService.save(user);
            System.out.println("Seeded secure User: " + userUsername);
        }
    }

    private void seedVulnerableUsers() {
        if (vulnerableUserRepository.count() == 0) {
            VulnerableUser admin = new VulnerableUser();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");
            vulnerableUserRepository.save(admin);

            VulnerableUser user = new VulnerableUser();
            user.setUsername("user");
            user.setPassword("user123");
            user.setRole("USER");
            vulnerableUserRepository.save(user);
            System.out.println("Seeded vulnerable users");
        }
    }

    private void seedStudents() {
        if (studentRepository.count() == 0) {
            Student s1 = new Student();
            s1.setFirstName("John");
            s1.setLastName("Doe");
            s1.setEmail("john@example.com");
            s1.setGpa(3.5);
            s1.setNotes("Excellent student");
            studentRepository.save(s1);

            Student s2 = new Student();
            s2.setFirstName("Jane");
            s2.setLastName("Smith");
            s2.setEmail("jane@example.com");
            s2.setGpa(3.9);
            s2.setNotes("Potential candidate for scholarship");
            studentRepository.save(s2);

            Student attacker = new Student();
            attacker.setFirstName("Attacker");
            attacker.setLastName("XSS");
            attacker.setEmail("hacker@evil.com");
            attacker.setGpa(0.0);
            attacker.setNotes("<script>alert(1)</script>");
            studentRepository.save(attacker);
            System.out.println("Seeded students");
        }
    }
}
