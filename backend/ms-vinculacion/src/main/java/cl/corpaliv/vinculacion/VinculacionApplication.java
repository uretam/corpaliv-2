package cl.corpaliv.vinculacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class VinculacionApplication {
    public static void main(String[] args) {
        SpringApplication.run(VinculacionApplication.class, args);
    }
}
