# CORPALIV 2.0

Proyecto académico Android nativo con Jetpack Compose, dos microservicios Spring Boot y MySQL. Programas, solicitudes, seguimiento y gestión usan servicios reales. Los tres programas iniciales son contenido académico de demostración; las solicitudes de prueba se guardan realmente en MySQL.

## Estructura

- `android/`: proyecto Kotlin y Jetpack Compose.
- `backend/ms-contenidos/`: servicio de programas, puerto 8081.
- `backend/ms-vinculacion/`: servicio de solicitudes y gestión, puerto 8082.
- `database/`: creación reproducible de dos bases y permisos mínimos.
- `docs/`: guía local y decisiones del proyecto.

## Inicio rápido

En esta PC, desde la raíz ejecuta `powershell -ExecutionPolicy Bypass -File .\iniciar-defensa.ps1`: inicia MySQL, ambos servicios, el emulador y la app. Lee también [docs/ejecucion-local.md](docs/ejecucion-local.md). El emulador usa `10.0.2.2` para acceder a los puertos locales 8081 y 8082. Los endpoints de salud en `127.0.0.1:8081/actuator/health` y `127.0.0.1:8082/actuator/health` deben indicar `UP`.

El APK académico firmado de demostración local está en `entregables/CORPALIV-2-firmado.apk`. Se construyó con la configuración debug para permitir conexión HTTP solo a `10.0.2.2` y se firmó con una llave académica propia. La llave y contraseña permanecen solo en `tmp/` privado. Para una publicación fuera del emulador se requiere HTTPS.

Flujo principal: Compose → ViewModel → Repository → Retrofit → Controller → Service → Repository JPA → MySQL. Las carpetas Android `data`, `ui`, `viewmodel` y las carpetas Spring `controller`, `dto`, `model`, `repository`, `service` siguen los ejemplos del profesor. [Guía breve para la defensa](docs/guia-defensa-codigo.md). El feriado se consulta por una API externa HTTPS independiente.

Resultados: [Sprint 0](docs/evidencia-sprint-0.md), [Sprint 1](docs/evidencia-sprint-1.md), [Sprint 2](docs/evidencia-sprint-2.md) y [Sprint 3: pruebas y cobertura](docs/evidencia-sprint-3.md). Los PDF/ZIP académicos del profesor y los archivos privados permanecen fuera de este repositorio.

## Autoría y alcance

El historial Git refleja únicamente quien realizó cada cambio. No se atribuyen tareas ni commits al otro integrante. Los contenidos, imágenes y datos de evaluación deben ser ficticios o estar autorizados.
