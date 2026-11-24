# CORPALIV 2.0

Proyecto académico Android nativo con dos microservicios Spring Boot y MySQL. **Sprint 1** incorpora la aplicación visual navegable en Jetpack Compose. Los programas, solicitudes y el acceso gestor usan datos ficticios en memoria, identificados dentro de la app. La conexión real con los servicios corresponde a Sprint 2.

## Estructura

- `android/`: proyecto Kotlin y Jetpack Compose.
- `backend/ms-contenidos/`: servicio de programas, puerto 8081.
- `backend/ms-vinculacion/`: servicio de solicitudes y gestión, puerto 8082.
- `database/`: creación reproducible de dos bases y permisos mínimos.
- `docs/`: guía local y decisiones del proyecto.

## Inicio rápido

Lee [docs/ejecucion-local.md](docs/ejecucion-local.md). Los endpoints de Sprint 0 son `GET http://localhost:8081/actuator/health` y `GET http://localhost:8082/actuator/health`. Devuelven 200 solo cuando el servicio puede consultar MySQL.

Resultados de Sprint 0: [docs/evidencia-sprint-0.md](docs/evidencia-sprint-0.md). Recorrido y capturas de Sprint 1: [docs/evidencia-sprint-1.md](docs/evidencia-sprint-1.md). Los PDF/ZIP académicos del profesor y los archivos privados permanecen fuera de este repositorio.

## Autoría y alcance

El historial Git refleja únicamente quien realizó cada cambio. No se atribuyen tareas ni commits al otro integrante. Los contenidos, imágenes y datos de evaluación deben ser ficticios o estar autorizados.
