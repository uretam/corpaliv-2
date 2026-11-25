# CORPALIV 2.0

Proyecto académico Android nativo con Jetpack Compose, dos microservicios Spring Boot y MySQL. **Sprint 2** conecta programas, solicitudes, seguimiento y gestión con los servicios locales. Los tres programas iniciales son contenido académico de demostración; las solicitudes de prueba se guardan realmente en MySQL.

## Estructura

- `android/`: proyecto Kotlin y Jetpack Compose.
- `backend/ms-contenidos/`: servicio de programas, puerto 8081.
- `backend/ms-vinculacion/`: servicio de solicitudes y gestión, puerto 8082.
- `database/`: creación reproducible de dos bases y permisos mínimos.
- `docs/`: guía local y decisiones del proyecto.

## Inicio rápido

Lee [docs/ejecucion-local.md](docs/ejecucion-local.md). Inicia MySQL y ambos servicios antes de abrir la app. El emulador usa `10.0.2.2` para acceder a los puertos locales 8081 y 8082. Comprueba `GET http://127.0.0.1:8081/actuator/health` y `GET http://127.0.0.1:8082/actuator/health`; `UP` incluye la conexión a MySQL.

Flujo principal: Compose → `AppViewModel` → `ApiRepository`/Retrofit → Controller → Service → Repository JPA → MySQL. `ms-contenidos` expone `GET /programas` y `GET /programas/{id}`. `ms-vinculacion` expone `POST /solicitudes`, `GET /seguimiento/{codigo}` y rutas `/admin/**` protegidas con Spring Security. El feriado se consulta por una API externa HTTPS independiente.

Resultados: [Sprint 0](docs/evidencia-sprint-0.md), [Sprint 1](docs/evidencia-sprint-1.md), [Sprint 2](docs/evidencia-sprint-2.md) y [Sprint 3: pruebas y cobertura](docs/evidencia-sprint-3.md). Los PDF/ZIP académicos del profesor y los archivos privados permanecen fuera de este repositorio.

## Autoría y alcance

El historial Git refleja únicamente quien realizó cada cambio. No se atribuyen tareas ni commits al otro integrante. Los contenidos, imágenes y datos de evaluación deben ser ficticios o estar autorizados.
