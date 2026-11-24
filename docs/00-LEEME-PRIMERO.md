# CORPALIV 2.0 | Paquete de preparación para Codex

## Orden de lectura
1. `01-PLAN-SPRINTS.md`: alcance, prioridades y comprobaciones.
2. `02-SISTEMA-VISUAL.md`: identidad y reglas de UI.
3. `03-PROMPT-MAESTRO-CODEX.md`: especificación y reglas de implementación.
4. `04-DEFENSA-EN-PAREJA.md`: demostración, reparto y preguntas.
5. `assets/`: referencias visuales complementarias.

## Uso rápido en tu PC
1. Extrae este directorio dentro del workspace de un **repositorio nuevo** de CORPALIV 2.0.
2. Los PDF originales del profesor y los ZIP de ejemplos de clase ya vienen incorporados en `referencias-academicas/`; pídele a Codex revisarlos antes de programar.
3. Abre **la carpeta raíz nueva** en el terminal de Codex y dile: "Lee 00-LEEME-PRIMERO.md y 03-PROMPT-MAESTRO-CODEX.md. Haz primero el inventario del entorno y el Sprint 0; después comienza Sprint 1. No avances de sprint ni hagas push sin mi confirmación."
4. La app Android se abre en Android Studio desde `android/`. Backend Spring Boot y MySQL se ejecutan con sus comandos Maven/servicios desde el terminal de Android Studio o PowerShell, tal como los documente Codex.
5. **No modificar** `https://github.com/uretam/CORPALIV`: es solo antecedente. No presentar como propios commits realizados por un agente u otro compañero.

## Decisión de base de datos — versión actualizada
**Usar exclusivamente MySQL**, no PostgreSQL, para el backend del proyecto. Se ha cambiado el plan, el prompt y el guion de defensa. Puerto local habitual: `3306`. En los diagramas ilustrativos de `assets/` puede aparecer la palabra “PostgreSQL” porque esas imágenes se generaron antes del cambio: **ignorar ese texto técnico y seguir la documentación escrita actualizada**. Las imágenes son referencias de estética, no fuente de verdad técnica.

## Estado de las referencias
Los diseños en `assets/` son propuestas conceptuales creadas para este proyecto, **no el logo oficial ni capturas reales de una app ejecutándose**. Algunos textos ilustrativos, dibujos de arquitectura o personas pueden diferir del resultado implementado. Cuando haya contradicción, manda este brief, la pauta y el código validado. Usar identidad institucional real y fotografías únicamente con autorización.

## Advertencias académicas
- El PDF `Ev. Parcial 4...` es de la evaluación parcial: sus requisitos son una base, pero las instrucciones específicas del Examen Final Transversal pueden añadir o cambiar condiciones.
- La propuesta técnica **no es totalmente consistente**: una sección recomienda dos microservicios de negocio y administración acotada; otra describe tres incluyendo administración. Se eligen dos + gestión protegida integrada por KISS, sujeto a la rúbrica final.
- La pauta exige 80% de cobertura de lógica móvil, APK firmado, GitHub/Trello y advierte sanciones por cambios o entrega fuera de plazo. Registrar evidencia real y respetar la hora fijada por el docente.
- No publicar credenciales reales, tokens, ni contraseñas de firma en repositorio público. Si el profesor requiere `.jks`, entregar solo un keystore académico exclusivo por canal apropiado; nunca usar una llave real de producción.
