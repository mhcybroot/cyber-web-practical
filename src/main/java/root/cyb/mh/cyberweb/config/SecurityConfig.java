package root.cyb.mh.cyberweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**",
                                                                "/h2-console/**")
                                                .permitAll()
                                                // Vulnerable endpoints might need public access or specific config
                                                // depending on
                                                // demo
                                                .requestMatchers("/vuln/sqli", "/secure/sqli").permitAll() // Open for
                                                                                                           // easy demo
                                                .requestMatchers("/vuln/xss/**", "/secure/xss/**").permitAll() // Open
                                                                                                               // for
                                                                                                               // easy
                                                                                                               // demo
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                // Disable CSRF for specific vulnerable endpoints to demonstrate the
                                                // vulnerability
                                                .ignoringRequestMatchers("/vuln/csrf/**"));

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
