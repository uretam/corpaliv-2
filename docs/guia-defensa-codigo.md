# Guía breve para defender el código

## Android: como el ejemplo `AppEjemplo`

`ui/screens` dibuja las pantallas Compose. Al pulsar un botón llama a `AppViewModel` en `viewmodel`. El ViewModel guarda el estado y pide datos a `ApiRepository` en `data/repository`. El repositorio usa las interfaces Retrofit de `data/remote`, y los datos JSON se representan con clases de `data/model`.

Recorrido fácil de mostrar: Programas → Detalle → Colaborar → Confirmación → Seguimiento. El formulario usa `FormularioValidator` antes de enviar. El código de seguimiento llega desde el servidor; Android no lo inventa.

## Backend: como `cliente-service` del profesor

Cada microservicio tiene `controller`, `dto`, `model`, `repository` y `service`:

- `controller`: define la ruta HTTP y la respuesta.
- `dto`: indica los campos que entran o salen por HTTP. Seguimiento devuelve solo `estado`.
- `model`: representa la tabla de MySQL mediante JPA.
- `repository`: consulta o guarda la entidad con Spring Data JPA.
- `service`: valida y aplica las reglas de negocio.

`ms-contenidos` lista los tres programas académicos de MySQL. `ms-vinculacion` guarda solicitudes, genera códigos UUID y permite al gestor cambiar estados. `config/SecurityConfiguration` protege `/admin/**` y usa BCrypt para la contraseña. La app se conecta al PC desde el emulador por `10.0.2.2`; MySQL solo lo consultan los servicios.

## Para practicar

Desde la raíz ejecuta `powershell -ExecutionPolicy Bypass -File .\iniciar-defensa.ps1`. En el menú de la app abre «Acceso gestor». El usuario y la contraseña de esta demo están en el archivo privado `tmp/db-env.ps1`, fuera de Git. Usa datos ficticios al presentar el formulario y conserva el código mostrado para consultar el estado.

El archivo `entregables/CORPALIV-2-firmado.apk` es el APK firmado probado en el emulador. Esta versión de demostración permite llegar a los microservicios del mismo PC. No presentar el HTTP local como configuración segura para Internet.
