# Sprint 1 — aplicación Android visual

Fecha de comprobación: 23 de noviembre de 2026. Alcance: interfaz y recorrido local; no incluye integración de Sprint 2.

## Entorno

- `emulator.exe -accel-check`: `WHPX(10.0.19045) is installed and usable`. Se usó Windows Hypervisor Platform.
- AVD de prueba `CORPALIV_Test_API_37`: `adb devices` mostró `emulator-5554 device`; `sys.boot_completed` devolvió `1`. El AVD anterior se conservó.
- Los dos servicios existentes respondieron `UP` en `http://127.0.0.1:8081/actuator/health` y `http://127.0.0.1:8082/actuator/health`, con MySQL local iniciado. Sprint 1 no cambió los microservicios.
- `android/gradlew.bat -p android :app:assembleDebug --offline`: `BUILD SUCCESSFUL`. APK debug instalada y abierta en el AVD.

## Recorrido manual comprobado

Inicio → Programas → Detalle → Colaborar. Un envío vacío mostró errores de nombre, correo, mensaje y consentimiento. Con datos ficticios válidos se abrió Confirmación con código temporal. Seguimiento consultó el código y mostró solo el estado. El menú secundario abrió Sobre CORPALIV y Acceso gestor. El panel temporal mostró la lista, el detalle, un cambio a `CONTACTADO`, su reflejo en Seguimiento y el diálogo de confirmación antes de eliminar. No se introdujeron claves reales. El reinicio del proceso devuelve la semilla ficticia; los datos de esta prueba no persisten.

Se revisaron las láminas originales de `assets/` frente a las capturas: barra superior azul, fondo crema, tarjetas blancas, CTA turquesa, estados verdes, navegación pública de tres destinos y menú secundario. Las tarjetas de programas incorporaron iconos Material nativos tras esa comparación. No se usaron las láminas como fondo ni el logo provisional como marca oficial.

También se comprobó temporalmente ancho de 360 dp y escala de fuente 1,3 en Programas, Detalle y Colaborar; el texto visible se ajustó sin cortes. El tamaño y escala del AVD se restauraron después de la prueba.

## Capturas reales del emulador

En [`capturas-sprint-1/`](capturas-sprint-1/) están Inicio, Programas, Detalle, Colaborar (parte superior e inferior), validación, Confirmación, Seguimiento, menú, Sobre, Login, Gestión, detalle gestor, cambio de estado, diálogo de eliminación y prueba de ancho reducido. Los archivos `13-*` son la prueba a 360 dp con fuente grande.

## Límite conocido

`DemoRepository` conserva programas y solicitudes solo en memoria. Login acepta cualquier par ficticio no vacío y **no autentica**; se advierte en pantalla. Ni seguimiento ni gestión consultan todavía a Spring Boot o MySQL. Esa integración pertenece a Sprint 2 y debe explicarse así en la defensa.
