# Sprint 3 — calidad técnica y simplicidad

Fecha: 24 de noviembre de 2026. Se conserva el diseño y la arquitectura del Sprint 2. Esta revisión focalizada se contrastó con el plan y los ZIP del profesor. `AppEjemplo` usa `PostViewModel` → `PostRepository` → Retrofit; `cliente-service` usa `ClienteService` → `ClienteRepository`. CORPALIV conserva esas relaciones y agrega solo la interfaz pequeña necesaria para probar el ViewModel.

## Problemas y correcciones

| Prioridad | Problema concreto | Corrección |
| --- | --- | --- |
| Alta | Android y Spring aceptaban teléfonos con reglas distintas. | `FormularioValidator` y `SolicitudService` usan el mismo formato opcional de 7 a 20 caracteres. Se alinearon también los límites de nombre y mensaje. |
| Alta | Un error HTTP 500 en seguimiento podía quedar sin mensaje; un error de servidor en login podía parecer contraseña incorrecta. | El ViewModel muestra error de consulta y el repositorio distingue `401` de otros códigos HTTP. |
| Media | El ViewModel instanciaba Retrofit internamente y era difícil probar sus decisiones. | Se inyecta una interfaz pequeña `AppRepository`; el constructor sin argumentos sigue creando `ApiRepository` en la app real. |
| Media | No había pruebas Android ni informe de cobertura. | Se añadieron JUnit, coroutines-test, MockWebServer y JaCoCo; también pruebas JUnit/Mockito de los servicios. |

## Comprobaciones reales

- Android: **11 pruebas unitarias pasadas**; compilación `:app:assembleDebug` exitosa. Pruebas de formulario válido/inválido, carga y detalle, creación, seguimiento y errores 404/429/500, login, CRUD y llamadas HTTP reales de Retrofit contra MockWebServer.
- JaCoCo, lógica Android (`AppViewModel`, `AppState`, `Pantalla`, `ApiRepository`, `FormularioValidator`): **93/107 líneas, 86,9%**. [XML de lógica](cobertura-logica-sprint-3.xml). Alcance definido por clases con lógica unitaria; no se excluyeron métodos dentro de ellas.
- JaCoCo, **toda la app** incluida la interfaz Compose: **103/343 líneas, 30,0%** en pruebas unitarias JVM. [XML completo](cobertura-app-sprint-3.xml). Las pantallas Compose no están cubiertas por esos tests; el 86,9% no debe presentarse como cobertura de toda la app.
- Java: **1 prueba de `ProgramaService` y 5 de `SolicitudService` pasadas**. Ambos proyectos empaquetaron con Maven. El aviso de Mockito sobre agente dinámico en JDK 21 no causó fallo.
- Emulador: **1 prueba instrumental pasada** sobre los servicios locales y MySQL: lista, detalle, creación de solicitud, seguimiento `RECIBIDA` y API externa de feriados. La fila creada desde Android apareció directamente en MySQL.
- Con esa fila se comprobó `401` sin autorización, login gestor `200`, actualización a `EN_REVISION` y seguimiento público con un único campo `estado`. Otra solicitud ficticia se eliminó con `204`; su seguimiento pasó a `404`.

## Límites

El emulador tuvo una pausa de System UI durante el arranque frío. La prueba instrumental terminó correctamente, pero la automatización por toques del teclado no completó una segunda entrada manual del formulario; no se cuenta como éxito. Las pruebas unitarias no cubren visualmente Compose. El APK firmado, las pruebas visuales finales y la preparación de entrega quedan para el sprint posterior solicitado por el usuario. No se hizo push en este sprint.

## Para la defensa

Integrante A: `FormularioValidator` valida antes de enviar; `AppViewModel` decide pantallas, estados y mensajes; `ApiRepository` usa Retrofit para llamar a los servicios. Integrante B: `SolicitudController` recibe HTTP, `SolicitudService` valida y genera UUID, JPA guarda en MySQL, Spring Security protege `/admin/**`. Ambos deben explicar que el seguimiento público devuelve solo el estado.
