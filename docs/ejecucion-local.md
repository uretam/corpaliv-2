# Ejecución local de CORPALIV 2.0

## Requisitos

- Android Studio con SDK Android 37 y un emulador configurado. Abrir la carpeta `android/` como proyecto independiente.
- JDK 17 o superior, Maven Wrapper y Gradle Wrapper con acceso a las distribuciones y dependencias.
- MySQL local en puerto 3306. No usar `root` como usuario de la aplicación.

En este PC se descargó JDK 17 y MySQL 8.4 portátiles bajo `tmp/` (ignorado por Git). Para ejecutar de nuevo el entorno preparado en esta misma carpeta, iniciar `tmp/mysql/mysql-8.4.13-winx64/bin/mysqld.exe --defaults-file=tmp/my.ini` desde la raíz del repositorio y cargar `. tmp/db-env.ps1` en cada terminal PowerShell antes de `mvnw.cmd`. Mantener `tmp/` fuera de Git: contiene credenciales y datos locales. En un clon nuevo, instalar MySQL y crear usuarios con el SQL indicado abajo.

## MySQL

1. Ejecutar `database/01-create-databases.sql` con un usuario administrador de MySQL.
2. Crear dos usuarios de aplicación distintos. Las sentencias `CREATE USER` y `GRANT` del SQL son ejemplos comentados: reemplazar los marcadores por contraseñas privadas en una consola segura, sin guardar el comando con claves en Git.
3. En PowerShell, configurar para **la sesión actual** las variables `CORPALIV_CONTENIDOS_DB_USER`, `CORPALIV_CONTENIDOS_DB_PASSWORD`, `CORPALIV_VINCULACION_DB_USER` y `CORPALIV_VINCULACION_DB_PASSWORD`. Las URL opcionales se llaman `CORPALIV_CONTENIDOS_DB_URL` y `CORPALIV_VINCULACION_DB_URL`.

## Servicios

Definir `JAVA_HOME` para un JDK 17 o superior (para Maven). Desde `backend/ms-contenidos/`: `./mvnw.cmd spring-boot:run`. Desde otra terminal en `backend/ms-vinculacion/`: `./mvnw.cmd spring-boot:run`. Comprobar `http://localhost:8081/actuator/health` y `http://localhost:8082/actuator/health`; deben responder 200 con `UP`. Si MySQL no está disponible, la aplicación no inicia o health responde 503. No interpretar un 200 sin comprobar la base. Los servicios escuchan solo en `127.0.0.1` para la demo local.

## Android

Abrir `android/` en Android Studio, configurar Gradle JDK 17, esperar la sincronización, seleccionar `CORPALIV_Test_API_37` y pulsar Run. También se puede compilar con `android/gradlew.bat -p android :app:assembleDebug`. El emulador de prueba quedó operativo con Windows Hypervisor Platform; `emulator.exe -accel-check` informa `WHPX ... installed and usable`. Si Android Studio muestra el AVD antiguo `Medium_Phone_API_37.0`, seleccionar el AVD de prueba, que se creó sin borrar el anterior.

La app de Sprint 1 usa datos ficticios en memoria: `DEMO-8F3K9D` sirve para probar Seguimiento. En el menú secundario se abre Acceso gestor; cualquier usuario y contraseña **ficticios y no vacíos** muestran el panel temporal. No introducir claves reales. Reiniciar el proceso de la app restablece los datos de ejemplo. En Sprint 2 la app utilizará `10.0.2.2:8081` y `10.0.2.2:8082` desde el emulador para llegar al host; en un teléfono físico se requiere otra dirección y red alcanzable. HTTP solo se permitirá en debug local cuando se conecten los servicios; producción requiere HTTPS.

## Límites de esta entrega

Sprint 1 no persiste solicitudes, no autentica ni consulta los microservicios. Las pantallas de gestión y seguimiento son una demostración local para revisar el flujo visual. No presentarlas como integración real.
