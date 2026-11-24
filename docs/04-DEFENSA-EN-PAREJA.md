# CORPALIV 2.0 | Defensa oral en pareja

## División real del trabajo (ajustar a lo que cada uno efectivamente implemente)
- **Integrante A (Android/experiencia):** presenta problema y recorrido, muestra inicio, programas, formulario, navegación y ViewModel/Retrofit.
- **Integrante B (backend/datos):** presenta entidades, Controller/Service/Repository, MySQL, Spring Security y CRUD de gestor.
- **Ambos:** revisan integración, pruebas, APK y conocen el flujo entero. Repartir commits y Trello según contribuciones reales; no atribuirse labores no realizadas.

## Guion de 5–7 minutos, adaptable
**0:00–0:45 | A.** «Nuestro proyecto resuelve la dispersión de información sobre CORPALIV y facilita expresar interés en colaborar. Diseñamos una app nativa Kotlin/Compose con navegación simple».
**0:45–2:10 | A.** Mostrar Inicio -> Programas -> Detalle -> Colaborar. Presentar formulario y validación, enviar una solicitud ficticia. «Compose muestra la interfaz; el ViewModel controla el estado y el Repository usa Retrofit para comunicarse por HTTP».
**2:10–2:35 | A.** Mostrar código de seguimiento, copiar, consultar estado sin datos personales.
**2:35–3:35 | B.** «Spring Boot recibe y valida datos. Controller expone la ruta; Service aplica reglas; Repository usa JPA para guardar en MySQL. Tenemos servicios separados de contenido y vinculación».
**3:35–4:50 | B.** Menú -> Acceso gestor -> login -> listado -> actualizar un estado -> salir. Repetir la consulta pública y observar el nuevo estado. «Los endpoints admin están protegidos por Spring Security; la consulta pública solo devuelve estado».
**4:50–5:30 | B.** Mostrar API externa feriados y comportamiento ante error; explicar que es separada de servicios propios.
**5:30–6:30 | A y B.** Mostrar ejecución de pruebas, porcentaje medido, README, GitHub/Trello reales, APK firmado e instalación comprobada. Cerrar con mejora de accesibilidad.

## Preguntas probables y respuestas breves
- **¿Por qué Kotlin/Compose?** Android nativo y UI declarativa que reacciona al estado.
- **¿Qué es ViewModel?** Mantiene el estado y las acciones de presentación; no dibuja botones ni consulta SQL.
- **¿Qué hace Retrofit?** Convierte métodos de ApiService en llamadas HTTP y transforma JSON según el convertidor.
- **¿Qué es Repository?** Centraliza el origen de datos para que ViewModel no dependa de detalles HTTP/SQL.
- **¿Por qué dos microservicios?** Separamos contenidos de solicitudes para dividir responsabilidades; gestión se mantiene dentro de vinculación para no crear otro servicio sin beneficio tangible. La pauta final puede precisar más.
- **¿Quién valida?** Ambos extremos: Android para informar al usuario y Spring Boot como control definitivo.
- **¿Cómo se protege el gestor?** Spring Security protege rutas; MySQL guarda un hash BCrypt. La clave local de demostración queda en `tmp/`, fuera de Git, y Android la conserva solo en memoria durante la sesión. HTTP Basic solo para demo local; HTTPS es obligatorio en un despliegue real.
- **¿Por qué un código opaco?** Para que el número interno no permita enumerar solicitudes y el visitante solo consulte su estado.
- **¿Por qué API externa?** Para mostrar feriados de Chile como información útil para planificar actividades; falla sin bloquear el CRUD propio.
- **¿Qué es CRUD?** Create, Read, Update, Delete: registrar/consultar/actualizar/eliminar.
- **¿Qué evidencia de calidad?** Tests JUnit y reporte auténtico de cobertura de lógica, además de comprobación de ejecución y APK firmado.
- **¿Por qué el emulador usa 10.0.2.2?** Es el alias de Android Emulator para llegar al loopback de la máquina anfitriona; `localhost` en Android apunta al propio emulador.

## Ensayo técnico obligatorio
1. Arrancar MySQL y ambos servicios sin errores.
2. Abrir app desde Android Studio o APK firmado instalado.
3. Comprobar Programas vía servicio.
4. Registrar solicitud ficticia -> obtener código.
5. Consultar estado público.
6. Login gestor -> editar estado.
7. Repetir seguimiento -> demostrar cambio real.
8. API externa con y sin conexión; pruebas unitarias; APK y README.

## Plan B honesto
Si un componente externo falla, mostrar estado de error controlado y explicar que no afecta los servicios propios. No simular respuestas reales. Si backend no inicia, mostrar logs y explicar el bloqueo; no afirmar funcionamiento inexistente.

## Regla final
Ambos deben saber explicar qué archivos participan en la operación completa «registrar solicitud» y «cambiar estado». Practicar preguntas cruzadas, no memorizar discursos sin entender el código.
