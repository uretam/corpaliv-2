package cl.corpaliv.vinculacion.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SeguimientoRateLimit extends OncePerRequestFilter {
    private final Map<String, ArrayDeque<Long>> consultas = new ConcurrentHashMap<>();
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        if (request.getMethod().equals("GET") && request.getRequestURI().startsWith("/seguimiento/")) {
            String ip = request.getRemoteAddr(); // No confiar en X-Forwarded-For sin proxy configurado.
            ArrayDeque<Long> tiempos = consultas.computeIfAbsent(ip, key -> new ArrayDeque<>());
            synchronized (tiempos) {
                long ahora = System.currentTimeMillis();
                while (!tiempos.isEmpty() && ahora - tiempos.peekFirst() > 60_000) tiempos.removeFirst();
                if (tiempos.size() >= 30) { response.sendError(429, "Demasiadas consultas"); return; }
                tiempos.addLast(ahora);
            }
        }
        chain.doFilter(request, response);
    }
}
