# Deadlineflow Legal Management

Sistema web para la gestión operativa simulada de expedientes, tareas, plazos, documentos y trazabilidad de cambios para un estudio jurídico ficticio.

## Estructura

- `backend/`: API REST construida con Spring Boot, JPA, Spring Security y JWT.
- `frontend/`: aplicación React, Vite y Tailwind CSS.

## Funcionalidades actuales

- Autenticación con JWT y control de acceso por roles.
- Registro y consulta de expedientes.
- Gestión de tareas, prioridades y validación de duplicidad.
- Gestión de plazos, anulación justificada y score de riesgo.
- Bitácora de eventos y consulta de carga de trabajo.
- Interfaz React para acceso, expedientes, tareas y balance de carga.

## Requisitos locales

- Java 17 o superior.
- Node.js 20 o superior.

## Ejecución

### Backend

```powershell
cd backend
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
.\mvnw.cmd spring-boot:run
```

El API se inicia en `http://localhost:8080`.

### Frontend

```powershell
cd frontend
npm install
npm run dev
```

La aplicación se inicia en `http://localhost:5173` y Vite redirige las solicitudes `/api` al backend local.

## Credenciales de desarrollo

| Usuario | Contraseña | Rol |
| --- | --- | --- |
| admin@deadlineflow.com | admin123 | ADMINISTRADOR |
| coordinador@deadlineflow.com | coord123 | COORDINADOR |
| abogado@deadlineflow.com | abog123 | ABOGADO |
| asistente@deadlineflow.com | asist123 | ASISTENTE |
| auditor@deadlineflow.com | audit123 | AUDITOR |

## Verificación

```powershell
cd backend
.\mvnw.cmd test

cd ../frontend
npm run build
```

## Convención de ramas

- `main`: versión estable.
- `develop`: integración de avances.
- `feature/*`: funcionalidades nuevas.
- `fix/*`: correcciones.
- `docs/*`: documentación del proyecto.
