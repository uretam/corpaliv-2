package cl.corpaliv.vinculacion.config;

import cl.corpaliv.vinculacion.model.Gestor;
import cl.corpaliv.vinculacion.repository.GestorRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfiguration {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService users(GestorRepository repo) {
        return username -> repo.findByNombreUsuarioAndActivoTrue(username)
            .map(g -> User.withUsername(g.nombreUsuario).password(g.passwordHash).roles("GESTOR").build())
            .orElseThrow(() -> new UsernameNotFoundException("Gestor no encontrado"));
    }
    @Bean CommandLineRunner seedGestor(GestorRepository repo, PasswordEncoder encoder,
        @Value("${CORPALIV_GESTOR_USER:}") String user,
        @Value("${CORPALIV_GESTOR_PASSWORD:}") String password) {
        return args -> { if (!user.isBlank() && !password.isBlank() && repo.findByNombreUsuarioAndActivoTrue(user).isEmpty())
            repo.save(new Gestor(user, encoder.encode(password))); };
    }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, SeguimientoRateLimit filtro) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/solicitudes").permitAll()
                .requestMatchers(HttpMethod.GET, "/seguimiento/*").permitAll()
                .requestMatchers("/admin/**").hasRole("GESTOR")
                .anyRequest().denyAll())
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(filtro, org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class)
            .build();
    }
}
