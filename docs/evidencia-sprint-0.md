# Sprint 0 - comprobaciones en este PC (23-11-2026)

## Entorno

- Android Studio presente en `C:\Program Files\Android\Android Studio`; SDK Android 37 existente y SDK 36 agregado para AGP 8.12.1. AVD `Medium_Phone_API_37.0` detectado.
- JDK 25 incluido en Android Studio. Gradle 8.13 no arrancó con él; se descargó JDK 17 portátil en `tmp/` y se usó para Android.
- Maven Wrapper 3.3.4 + Maven 3.9.11 descargados; no se requiere Maven global.
- MySQL 8.4.13 portátil se ejecutó en `127.0.0.1:3306`. Dos bases y dos usuarios de aplicación separados. Credenciales y datos quedaron en `tmp/`, ignorado por Git.
- Git disponible. GitHub CLI 2.105.0 instalado para el usuario actual; requiere inicio de sesión antes de crear el remoto.

## Resultado real

| Prueba | Resultado |
| --- | --- |
| `ms-contenidos/mvnw.cmd -DskipTests package` | BUILD SUCCESS |
| `ms-vinculacion/mvnw.cmd -DskipTests package` | BUILD SUCCESS |
| `android/gradlew.bat clean :app:assembleDebug` | BUILD SUCCESS; APK debug local ignorado |
| MySQL como usuario de contenidos | `SELECT DATABASE(), 1` = `corpaliv_contenidos`, `1` |
| MySQL como usuario de vinculación | `SELECT DATABASE(), 1` = `corpaliv_vinculacion`, `1` |
| `GET localhost:8081/actuator/health` | 200 `{"status":"UP"}` con consulta `SELECT 1` a MySQL |
| `GET localhost:8082/actuator/health` | 200 `{"status":"UP"}` con consulta `SELECT 1` a MySQL |
| Inicio del AVD | Bloqueado: falta Android Emulator Hypervisor Driver para x86_64 |

El emulador informó 8043 MiB de RAM disponible frente a 16384 MiB recomendados; esta es una advertencia adicional, no la causa inmediata del fallo. No se verificó instalación ni arranque de la app en Android. El APK construido es debug, no el APK firmado exigido al final.

## Decisiones

Se conservan dos microservicios y MySQL. La propuesta del profesor alterna entre dos y tres servicios; el alcance escrito del proyecto integra la gestión en `ms-vinculacion`. Los ZIP de clase ilustran capas sencillas, pero contienen dependencias duplicadas y configuración `root` sin contraseña, que no se reprodujeron.
