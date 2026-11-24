# CORPALIV 2.0 | Plan ejecutable para la evaluación

## Alcance inamovible del MVP
Android nativo, visitante sin cuenta, acceso gestor por menú secundario.

**Pantallas:**
1. Inicio: propósito de CORPALIV / Alba Lab, dos CTA, programas destacados, sin métricas inventadas.
2. Programas: lista cargada por Retrofit desde `ms-contenidos`, indicador de carga, vacío, error, detalle.
3. Colaborar: selector visual de Donación, Voluntariado, Alianza, Capacitación y formulario validado (nombre, correo, teléfono opcional, mensaje, consentimiento de privacidad).
4. Confirmación: mostrar resultado real del backend con código aleatorio generado por el servidor; copiar código; volver al inicio o seguimiento.
5. Seguimiento: código opaco y consulta pública **solo del estado**, sin datos personales ni IDs internos.
6. Acceso gestor: usuario/contraseña; panel con lista, detalle, edición de estado/datos permitidos y eliminación confirmada; logout.

**Fuera de alcance:** pagos, chat IA, push, perfiles extra, foto de beneficiarios, offline Room, mapa, catálogo comercial, panel web y nuevas funcionalidades no pedidas.

## Arquitectura y contratos
```
android/ (Kotlin + Compose + MVVM + Retrofit + Gson)
  Screen -> ViewModel -> Repository -> ApiService/Retrofit
backend/ms-contenidos/ (Java + Spring Boot + Maven + Spring Data JPA)
  Controller -> Service -> Repository -> Programa
backend/ms-vinculacion/ (Java + Spring Boot + Maven + Spring Security + JPA)
  Controller -> Service -> Repository -> Solicitud / Gestor
MySQL: una instancia local con dos bases de datos independientes, `corpaliv_contenidos` y `corpaliv_vinculacion` (una por microservicio). Cada servicio debe operar únicamente sobre su base; no compartir tablas. Utilizar MySQL Connector/J mediante dependencia Maven `com.mysql:mysql-connector-j` compatible con Spring Boot.
```

**Configuración local mínima:** `spring.datasource.url=jdbc:mysql://localhost:3306/corpaliv_contenidos` para contenidos y `jdbc:mysql://localhost:3306/corpaliv_vinculacion` para vinculación. Usuario/contraseña por variables de entorno o configuración local excluida de Git; no usar `root` como credencial de la aplicación. Crear ambas bases antes de iniciar servicios o documentar el procedimiento reproducible. No habilitar conexión remota a MySQL para el emulador: Android consulta únicamente a los servicios Spring Boot.

**Puerto sugerido (ajustable al comprobar conflictos):** contenidos 8081; vinculación 8082; MySQL 3306. Android Emulator usa `http://10.0.2.2:8081/` y `http://10.0.2.2:8082/`, no `localhost`, para conectar con el host. Permitir HTTP sin cifrado **solo en debug local**; una solución real exige HTTPS. En dispositivo físico, la dirección cambia.

**Entidades mínimas:**
- Programa: id, nombre, resumen, descripcion, categoria, imagenRecurso, activo. Datos de ejemplo verificables y sin afirmar programas/impactos oficiales no acreditados.
- Solicitud: id interno, codigoSeguimiento opaco (UUID aleatorio v4 o equivalente fuerte), nombre, correo, telefono opcional, modalidad, mensaje, aceptaPrivacidad, estado, fechaCreacion. Estado inicial `RECIBIDA`.
- Gestor: id, nombreUsuario, passwordHash BCrypt, activo. Inicialización únicamente desde configuración local o mecanismo de semillas seguro.

**Endpoints sugeridos:**
- GET `/programas`; GET `/programas/{id}` (públicos)
- POST `/solicitudes` (público, validado); devuelve 201 y código
- GET `/seguimiento/{codigo}` (público, devuelve exclusivamente estado y fecha mínima; códigos opacos, respuesta genérica ante inexistentes, protección contra intentos masivos)
- GET `/admin/sesion` (validación de credenciales; 200/401)
- GET `/admin/solicitudes`; GET `/admin/solicitudes/{id}`; PUT `/admin/solicitudes/{id}`; DELETE `/admin/solicitudes/{id}` (autorizados)

Login: implementar `HTTP Basic` con Spring Security para entorno académico, mediante pantalla Compose que valida credenciales con ruta `/admin/sesion`; no persistir contraseña en disco o repositorio, mantenerla solo durante sesión en memoria. No usar JWT solo por complejidad. El backend, no la visibilidad de botones, aplica la autorización. Para producción HTTPS sería obligatorio.

**API externa**: `GET https://api.roison.cl/feriados.json`; mostrar «Próximos feriados» con Retrofit en una pequeña tarjeta informativa de Colaborar o Programas. Si falla, mensaje de indisponibilidad y el resto de la app sigue funcionando. No mezclar este servicio con la base propia.

## Estrategia de sprints y checkpoints
### Sprint 0 — Inventario y base ejecutable (previo)
- Verificar herramientas instaladas en PC (Android Studio/SDK/emulador, JDK17+, Gradle Wrapper, Java/Maven, MySQL); comprobar puertos y memoria.
- Revisar PDFs y ejemplos de clase, sin copiarlos a ciegas. Corregir duplicaciones y errores de los ejemplos.
- Crear repositorio nuevo y `.gitignore` adecuado, una rama de trabajo, `README` mínimo.
- Iniciar un proyecto Android `Empty Activity` en Compose y dos esqueletos Spring Boot compatibles. Encender MySQL y hacer smoke test **antes de invertir tiempo en estética**.
**Puerta:** app vacía inicia, ambos servicios devuelven health 200, DB conecta; capturas de logs/outputs.

### Sprint 1 — Producto visual navegable
- Implementar tokens de tema del archivo visual; pantallas Inicio, Programas, Detalle, Colaborar, Confirmación, Seguimiento, Login Gestor y Panel.
- Mantener datos **ficticios explícitamente temporales** hasta conectar endpoints. Reutilizar componentes simples; nada de pantallas decorativas no funcionales.
- Wireframes y luego implementación de una sola identidad; iconografía Material, arte genérico con permisos; contenido legible, contraste, tamaños táctiles.
**Puerta:** Gradle compila, navega sin crashes; el compañero y el usuario pueden explicar `MainActivity`, `App`, pantalla y estado Compose.

### Sprint 2 — Funcionalidad real e integración
- Crear entidades, repositorios, servicios y controladores, con SQL/semillas repetibles y validaciones backend.
- Conectar programas y solicitudes Android por Retrofit a los dos microservicios. API externa de feriados con respuesta de error controlada.
- Implementar código de seguimiento, consulta de estado público minimizada, Spring Security y CRUD de gestión. Probar éxito, 400, 401, 404, 500 y pérdida de conectividad.
- Mantener una única configuración de URL por entorno, sin hardcodear contraseñas; probar persistencia tras reinicio de Spring.
**Puerta:** formulario real -> registro MySQL -> código -> seguimiento -> login -> actualización de estado -> seguimiento refleja nuevo estado; todas las rutas admin devuelven 401 a visitante.

### Sprint 3 — Evaluación y evidencia
- Tests Android de validadores, ViewModels y Repository con fakes/mocks; cobertura JaCoCo **>=80% de la lógica evaluable** y reporte conservado. Pruebas backend JUnit/Mockito en Service; no fabricar métricas de cobertura.
- Inspeccionar app en emulador Android real; probar lista, formulario, datos inválidos, sesiones, estado vacío, errores HTTP y pantalla pequeña.
- Generar APK **firmado** e instalarlo, verificar firma y demo después de instalación. Usar keystore académico exclusivamente y gestionar secreto fuera de repositorio público.
- README con nombres, descripción, instrucciones, endpoints, captura técnica, pruebas, evidencias de APK; Trello real y commits de ambos autores.
- Ensayo cronometrado de defensa con un registro y un cambio de estado completos.
**Puerta:** compilación y prueba documentadas; demo repetible; ambos pueden explicar el mismo flujo completo.

## Prioridades si el tiempo aprieta
1. App que compile y abra, backend MySQL funcional, CRUD real y Retrofit.
2. API externa y login protegido con seguimiento seguro.
3. Pruebas/cobertura, APK firmado, documentación y evidencias.
4. Pulido visual secundario.
**No sustituir una función requerida por una animación; no declarar probado lo que no se ejecutó.**
