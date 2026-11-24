# Sprint 2 — backend e integración real

Comprobado el 24 de noviembre de 2026 en Windows, MySQL 8.4 local, dos servicios Spring Boot y emulador `CORPALIV_Test_API_37` con Windows Hypervisor Platform.

## Recorrido comprobado

- Ambos servicios compilaron con Maven y respondieron `UP` en 8081/8082 con la comprobación de MySQL activa. La app compiló con Gradle, se instaló y abrió en el emulador.
- Android recibió tres programas de `ms-contenidos`. Se abrió el detalle de Educación inclusiva; el detalle usa `GET /programas/{id}`. Los tres programas están guardados en MySQL y marcados como contenido académico demostrativo.
- El formulario Android creó una solicitud de prueba en `ms-vinculacion`. El servidor devolvió un UUID de seguimiento; la fila se comprobó directamente en MySQL. La consulta pública mostró `RECIBIDA` sin nombre, correo ni otros datos personales.
- Se probó el rechazo de una contraseña incorrecta. Un gestor autenticado abrió la lista, cambió el estado a `CONTACTADO`, editó un nombre y eliminó otra solicitud de prueba con confirmación. MySQL reflejó los cambios. Tras reiniciar `ms-vinculacion` y la app, la solicitud restante conservó `CONTACTADO`.
- Sin credenciales, las rutas administrativas GET, PUT y DELETE respondieron `401`; una solicitud vacía respondió `400`; un seguimiento inexistente respondió `404`. En 33 consultas seguidas, el límite por IP produjo respuestas `429`. La contraseña del gestor se observó en MySQL solo como hash BCrypt (`$2a$`).
- La app recibió desde `https://api.roison.cl/feriados.json` un próximo feriado chileno válido y lo mostró dentro de Programas. El error de red tiene mensaje y botón de reintento; esa rama no se forzó manualmente en el emulador.

## Capturas reales

En [capturas-sprint-2](capturas-sprint-2/) están Inicio, Programas con feriado, detalle, Colaborar, Confirmación, Seguimiento, error de login, Gestión, actualización, edición y eliminación. Son capturas del emulador; los datos de formulario son ficticios. Las imágenes de las referencias visuales se usaron como guía de componentes Compose, no como fondos.

## Límites y defensa

El backend escucha solo en `127.0.0.1`. La app debug permite HTTP únicamente hacia `10.0.2.2`; HTTP Basic requiere HTTPS fuera de la demostración local. El límite de consultas vive en memoria y aplica por IP, suficiente para el entorno de evaluación local pero no para un servidor distribuido. Hibernate `ddl-auto=update` mantiene las tablas en esta demo; una instalación real necesita migraciones versionadas y una configuración de red/secretos propia.

Integrante A puede explicar Compose, estado del `AppViewModel`, validación del formulario y Retrofit. Integrante B puede explicar Controller → Service → Repository JPA → MySQL, seguimiento mínimo y Spring Security. Ambos deben poder repetir el flujo formulario → código → seguimiento → gestión → nuevo estado.
