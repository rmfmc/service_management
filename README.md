# Service Management API
API REST para la gestión operativa de un servicio técnico de electrodomésticos. Permite administrar usuarios internos, clientes, direcciones, aparatos instalados, catálogos auxiliares, avisos de trabajo y cargos económicos asociados.

## Tabla de contenidos
- [Qué resuelve este proyecto](#qué-resuelve-este-proyecto)
- [Stack tecnológico](#stack-tecnológico)
- [Módulos principales](#módulos-principales)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Arranque rápido](#arranque-rápido)
- [Configuración](#configuración)
- [Primer acceso y datos de prueba](#primer-acceso-y-datos-de-prueba)
- [Autenticación y roles](#autenticación-y-roles)
- [Formato de respuestas](#formato-de-respuestas)
- [Resumen de endpoints](#resumen-de-endpoints)
- [Documentación adicional](#documentación-adicional)
- [Licencia](#licencia)

## Qué resuelve este proyecto
La API cubre el flujo completo de un servicio técnico:

- Gestión de usuarios internos con roles `ADMIN` y `TECH`.
- Alta y mantenimiento de clientes finales.
- Gestión de direcciones asociadas a cada cliente.
- Registro de electrodomésticos instalados en cada dirección.
- Catálogos maestros para marcas, tipos de aparato y averías comunes.
- Creación y seguimiento de avisos de trabajo (`work orders`).
- Registro de cargos económicos por aviso.
- Protección de endpoints mediante JWT y reglas de autorización por rol.

## Stack tecnológico
- **Java 21**.
- **Spring Boot 4**.
- Spring Web MVC.
- Spring Security.
- Spring Data JPA.
- Jakarta Validation.
- MySQL 8.
- JJWT 0.13.0.
- Maven Wrapper (`./mvnw`).
- Docker Compose para entorno local de base de datos.

## Módulos principales
- **Autenticación**: login, emisión de JWT y filtro de seguridad.
- **Usuarios**: administración de personal interno y credenciales.
- **Clientes**: fichas de cliente y datos de contacto.
- **Direcciones**: domicilios vinculados a clientes.
- **Electrodomésticos**: aparatos instalados por dirección.
- **Catálogos**: marcas, tipos y averías frecuentes.
- **Avisos**: órdenes de trabajo, seguimiento técnico y planificación.
- **Cargos**: importes, método de pago, pagador y estado de cobro.

## Estructura del proyecto
```text
src/main/java/ruben/springboot/service_management
├── authentication/   # Login, JWT y utilidades de seguridad
├── config/           # Seguridad, propiedades y datos iniciales
├── controllers/      # Endpoints REST
├── errors/           # Excepciones y manejador global
├── models/           # Entidades, DTOs, enums y mappers
├── repositories/     # Acceso a datos con JPA
└── services/         # Lógica de negocio
```

Documentación funcional y técnica:
```text
docs/
├── API_REFERENCE.md
├── ARCHITECTURE.md
├── DATA_MODEL.md
└── GITHUB_SETUP.md
```

## Requisitos
- JDK 21.
- Maven 3.9+ o uso de `./mvnw`.
- MySQL 8+ si no usa contenedores.
- Docker + Docker Compose si prefiere usar el `docker-compose.yml` del repositorio.

## Arranque rápido
1. Levantar MySQL
```bash
docker compose up -d
```

2. Crear su configuración local
```bash
cp application-local.example.properties src/main/resources/application-local.properties
```

3. Revisar los valores
El archivo de ejemplo ya apunta a la base local creada por Docker Compose:

```properties
DB_URL=jdbc:mysql://localhost:3306/service_management?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=root
JWT_SECRET=change_this_secret_key_with_at_least_32_chars
JWT_EXP_MINUTES=180
```

4. Arrancar la aplicación
```bash
./mvnw spring-boot:run
```

### 5. Probar el login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123456"
}
```

### 6. Detener MySQL cuando termine
```bash
docker compose down
```

## Configuración
La aplicación lee dos grupos de propiedades:

### Conexión y seguridad
Se resuelven desde variables o propiedades equivalentes:
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXP_MINUTES`

### Datos de arranque
Variables opcionales para el primer acceso y los datos demo (solo si quiere cambiar los valores por defecto definidos en el backend):
- `APP_INITIAL_ADMIN_ENABLED`
- `APP_INITIAL_ADMIN_NAME`
- `APP_INITIAL_ADMIN_PHONE`
- `APP_INITIAL_ADMIN_USERNAME`
- `APP_INITIAL_ADMIN_PASSWORD`
- `APP_DEMO_DATA_ENABLED`

### Opción recomendada: archivo local no versionado
El flujo más cómodo para desarrollo es usar un archivo local no versionado:
```bash
cp application-local.example.properties src/main/resources/application-local.properties
```

Ese archivo le permite mantener la configuración junto al proyecto sin exponer secretos reales al repositorio.

### Opción alternativa: variables de entorno
También puede exportar los valores directamente:

```bash
export DB_URL='jdbc:mysql://localhost:3306/service_management?useSSL=false&serverTimezone=UTC'
export DB_USER='root'
export DB_PASSWORD='root'
export JWT_SECRET='change_this_secret_key_with_at_least_32_chars'
export JWT_EXP_MINUTES='180'
export APP_INITIAL_ADMIN_USERNAME='admin'
export APP_INITIAL_ADMIN_PASSWORD='admin123456'
export APP_DEMO_DATA_ENABLED='false'
```


## Primer acceso y datos de prueba
### Admin inicial
La aplicación crea automáticamente un usuario administrador si la base de datos arranca vacía.

### Comportamiento por defecto
- Si no existe ningún usuario, se crea un admin inicial.
- Si ya existe al menos un usuario, no se crea nada;

Valores por defecto:
- `username`: `admin`
- `password`: `admin123456`

> Recomendación: cambie esa contraseña en cuanto arranque la aplicación por primera vez fuera de un entorno local.

### Datos de demo opcionales
Si activa `APP_DEMO_DATA_ENABLED=true`, además del admin inicial se cargan catálogos básicos de prueba:
- marcas (`Balay`, `Bosch`, `Fagor`)
- tipos de aparato (`Lavadora`, `Frigorífico`, `Lavavajillas`)
- averías comunes asociadas a esos tipos

Esto es útil para desarrollo o demos, pero normalmente no debería dejarse activado en producción.

## Autenticación y roles
### Login
- Endpoint público: `POST /api/auth/login`.
- Recibe `username` y `password`.
- Devuelve un JWT y los datos mínimos del usuario autenticado: `token`, `userId`, `username`, `role`.

Ejemplo:
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123456"
}
```

### Uso del token
Envíe el JWT en la cabecera `Authorization`:

```http
Authorization: Bearer <jwt>
```

### Roles
- `ADMIN`: acceso total sobre usuarios, clientes, direcciones, electrodomésticos, catálogos, avisos y cargos.
- `TECH`: acceso a operaciones técnicas sobre órdenes asignadas y gestión parcial de cargos.

## Formato de respuestas
### Respuesta paginada
Los endpoints paginados devuelven un objeto `PageResponse` con esta estructura:

```json
{
  "content": [],
  "currentPage": 0,
  "pageSize": 10,
  "totalItems": 42,
  "itemsInPage": 10,
  "totalPages": 5,
  "first": true,
  "last": false
}
```

### Respuesta de error
Los errores de validación, negocio y seguridad se normalizan en una forma similar a esta:

```json
{
  "error": "BAD_REQUEST",
  "message": "La validación del request falló",
  "path": "/api/clients",
  "status": 400,
  "timestamp": "2026-03-18T10:00:00Z",
  "errors": {
    "name": "el nombre es obligatorio"
  }
}
```

## Resumen de endpoints
La referencia completa está en [docs/API_REFERENCE.md](docs/API_REFERENCE.md).

- Auth: `/api/auth/*`
- Usuarios: `/api/users/*`
- Clientes: `/api/clients/*`
- Direcciones: `/api/addresses/*`
- Electrodomésticos: `/api/appliances/*`
- Marcas: `/api/brands/*`
- Tipos de aparato: `/api/appliance-types/*`
- Averías comunes: `/api/common-faults/*`
- Avisos: `/api/work-orders/*`
- Cargos: `/api/charges/*`

## Documentación adicional
- Guía completa de endpoints: [docs/API_REFERENCE.md](docs/API_REFERENCE.md)
- Arquitectura y seguridad: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)
- Modelo de datos: [docs/DATA_MODEL.md](docs/DATA_MODEL.md)
- Recomendaciones para GitHub: [docs/GITHUB_SETUP.md](docs/GITHUB_SETUP.md)

## Licencia
Consulte el archivo [LICENSE](LICENSE).