# Ejecución local de CORPALIV 2.0

En esta PC puedes iniciar todo con `powershell -ExecutionPolicy Bypass -File .\iniciar-defensa.ps1` desde la raíz. El script usa solo los archivos privados locales bajo `tmp/` y abre la app en el emulador visible. Para la explicación del código, lee [guia-defensa-codigo.md](guia-defensa-codigo.md).

## Requisitos

- Android Studio con SDK Android 37 y un emulador configurado. Abrir la carpeta `android/` como proyecto independiente.
- JDK 17 o superior, Maven Wrapper y Gradle Wrapper con acceso a las distribuciones y dependencias.
- MySQL local en puerto 3306. No usar `root` como usuario de la aplicación.

En este PC se preparó MySQL 8.4 portátil bajo `tmp/` (ignorado por Git). Para reutilizarlo, iniciar `tmp/mysql/mysql-8.4.13-winx64/bin/mysqld.exe --defaults-file=tmp/my.ini` desde la raíz del repositorio y cargar `. .\tmp\db-env.ps1` en cada terminal PowerShell antes de iniciar los servicios. Este archivo privado también define el usuario y contraseña del gestor de demostración; consúltalo localmente para la defensa. Mantener `tmp/` fuera de Git: contiene credenciales y datos locales. En un clon nuevo, instalar MySQL y crear usuarios con el SQL indicado abajo.

## MySQL

1. Ejecutar `database/01-create-databases.sql` con un usuario administrador de MySQL.
2. Crear dos usuarios de aplicación distintos. Las sentencias `CREATE USER` y `GRANT` del SQL son ejemplos comentados: reemplazar los marcadores por contraseñas privadas en una consola segura, sin guardar el comando con claves en Git.
3. En PowerShell, configurar para **la sesión actual** las variables `CORPALIV_CONTENIDOS_DB_USER`, `CORPALIV_CONTENIDOS_DB_PASSWORD`, `CORPALIV_VINCULACION_DB_USER` y `CORPALIV_VINCULACION_DB_PASSWORD`. Las URL opcionales se llaman `CORPALIV_CONTENIDOS_DB_URL` y `CORPALIV_VINCULACION_DB_URL`. Para crear el gestor inicial, definir `CORPALIV_GESTOR_USER` y `CORPALIV_GESTOR_PASSWORD` antes del primer arranque de `ms-vinculacion`. El servicio guarda un hash BCrypt, nunca la contraseña en texto plano. En arranques posteriores, las credenciales del gestor deben coincidir con el registro ya creado en MySQL.

## Servicios

Definir `JAVA_HOME` para un JDK 17 o superior (en este PC funciona `C:\Users\ASPIRINA\.jdks\jbr-21.0.11`). Desde `backend/ms-contenidos/`: `.\mvnw.cmd spring-boot:run`. Desde otra terminal en `backend/ms-vinculacion/`: `.\mvnw.cmd spring-boot:run`. Comprobar `http://127.0.0.1:8081/actuator/health` y `http://127.0.0.1:8082/actuator/health`; deben responder 200 con `UP`, incluida la comprobación de MySQL. Si MySQL no está disponible, la aplicación no inicia o health responde 503. Los servicios escuchan solo en `127.0.0.1` para la demo local. Hibernate crea/actualiza las tablas durante el arranque local; no hay migraciones de producción en este sprint.

## Android

Abrir `android/` en Android Studio, configurar Gradle JDK 21, esperar la sincronización, seleccionar `CORPALIV_Test_API_37` y pulsar Run. También se puede compilar con `android/gradlew.bat -p android :app:assembleDebug`. El emulador de prueba quedó operativo con Windows Hypervisor Platform; `emulator.exe -accel-check` informa `WHPX ... installed and usable`. Si Android Studio muestra el AVD antiguo `Medium_Phone_API_37.0`, seleccionar el AVD de prueba, que se creó sin borrar el anterior. El primer arranque del AVD puede ser lento.

La app de Sprint 2 usa `10.0.2.2:8081` y `10.0.2.2:8082` desde el emulador para llegar al PC. En un teléfono físico se requiere otra dirección y red alcanzable. Inicio y Programas consultan `ms-contenidos`; enviar el formulario crea una solicitud en `ms-vinculacion` y devuelve un código UUID. El menú secundario ofrece Seguimiento y Acceso gestor. El acceso gestor comprueba realmente usuario y contraseña; las credenciales permanecen solo en memoria mientras la sesión está abierta. El feriado se consulta por HTTPS a `api.roison.cl` y tiene mensaje de error/reintento independiente. El HTTP sin cifrar a `10.0.2.2` está permitido solo en la variante debug; un despliegue real requiere HTTPS para los servicios, especialmente antes de usar HTTP Basic.

## Límites de esta entrega

Las semillas de programas son académicas y no afirman datos institucionales reales. El hash del gestor se crea con variables de entorno solo si todavía no existe ese usuario. El límite de seguimiento es de 30 peticiones por IP y minuto en memoria; se reinicia con el servicio y no es distribuido. Para cambiar la URL del backend o usar un dispositivo físico hay que ajustar `ApiRepository.kt` y la configuración de red debug. No se ha preparado despliegue público ni HTTPS para el backend.

## Pruebas de Sprint 3

Con JDK 21 y el SDK configurados, ejecutar desde la raíz `android/gradlew.bat -p android :app:testDebugUnitTest :app:jacocoLogicReport :app:jacocoAppReport :app:assembleDebug`. Los informes HTML quedan en `android/app/build/reports/jacoco/`; los XML verificados se conservaron en `docs/`. Para la prueba integral, iniciar MySQL, ambos servicios y el AVD antes de `android/gradlew.bat -p android :app:connectedDebugAndroidTest`. Esta prueba crea una solicitud ficticia real y consulta la API externa; requiere conectividad. En cada servicio, `.\mvnw.cmd test` ejecuta las pruebas Java.
