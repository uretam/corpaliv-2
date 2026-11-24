# CORPALIV 2.0 | Sistema visual (fuente de verdad para Codex)

## Objetivo
Institucional moderno + humano y cercano. Inspiración: las tres láminas `assets/` de esta carpeta. Aun cuando las imágenes IA muestren variaciones, **estos valores y jerarquías escritos prevalecen**. Todo debe construirse como componentes nativos de Jetpack Compose, no capturas puestas como fondo ni WebView.

## Tokens
- Principal azul marino `#173C64`: barra superior, títulos, texto jerárquico.
- Turquesa `#168B9B`: CTA principal, selección, estados interactivos.
- Verde `#286650`: confirmación, éxito y acentos relacionados con impacto social.
- Crema `#F8F3E9`: fondo general.
- Blanco `#FFFFFF`: tarjetas y superficies.
- Texto oscuro `#173C64` sobre fondo claro. Evitar blanco sobre colores claros y verificar contraste WCAG AA.
- Roboto normal/medium/bold; respetar escalado de fuente del sistema.
- Grilla de espaciado 4 / 8 / 12 / 16 / 24 / 32 dp. Horizontal 20dp en pantallas normales; 16dp en dispositivos estrechos.
- Bordes: tarjetas 16dp, controles/botones 12-16dp, chips 50% de alto.
- Taps mínimos 48x48dp; botón CTA preferido alto 52dp.
- Barra superior azul estable; barra inferior SOLO `Inicio`, `Programas`, `Colaborar` en experiencia pública; menú secundario `Seguimiento`, `Sobre CORPALIV`, `Acceso gestor`. En panel gestor se permite navegación de regreso y cierre de sesión sin barra pública obligatoria.
- Motion: `AnimatedVisibility` o `animateContentSize` en casos simples, 150–250ms; respetar reducir animaciones si es factible. No inventar animaciones complejas.

## Lenguaje de componentes
**Inicio:** hero con titular corto, texto humano y dos CTA; destacados de programas; barra superior de marca. Primera acción evidente.
**Programas:** lista tarjetas blancas con ícono/ilustración sencilla, título, resumen y flecha; detalle con CTA.
**Colaborar:** cuatro opciones tipo tarjeta seleccionable, campos con etiquetas permanentes, indicadores de requerido, validación debajo de cada campo y botón enviar que muestre progreso; no enviar antes de respuesta 201.
**Confirmación:** check verde, texto de confirmación, código de seguimiento completo y copiar; sin mostrar datos sensibles.
**Seguimiento:** formulario con campo código, botón consultar, estado visual. Nunca renderizar nombre/correo/mensaje en respuesta pública.
**Gestor:** login sobrio; lista de solicitudes filtrable por estados, detalle, cambiar estado, editar/eliminar con diálogo de confirmación.

## Copy sugerido
- Hero: «Conecta, conoce y colabora».
- Texto: «Descubre cómo apoyar la inclusión, la educación y la formación para el trabajo».
- CTA: «Quiero colaborar» / «Conocer programas».
- Status: `RECIBIDA`, `EN_REVISION`, `CONTACTADO`, `CONCRETADO`, `DESCARTADO`, mostrados con palabras legibles.
- Evitar métricas de impacto o historias reales no proporcionadas por CORPALIV.

## Fotos y logo
El logo de las imágenes es **provisional generado**, no atribuir como logo oficial. Si el docente/usuario facilita logo real, usarlo con autorización. Evitar fotografías de beneficiarios reales o datos sensibles; preferir ilustraciones genéricas originales o imágenes oficiales autorizadas. No rasterizar pantallas completas.

## Criterios visuales de aceptación
- Capturas del emulador por pantalla; ninguna pantalla parece pertenecer a otra aplicación.
- Sin textos cortados en 360dp de ancho y con fuente grande de Android.
- Estados loading/error/empty/success diseñados.
- Los botones principales tienen contraste, tamaño táctil y acciones funcionales.
- Ningún hardcoded dummy visible al finalizar Sprint 2 salvo seeds institucionales demostrativos identificados.
