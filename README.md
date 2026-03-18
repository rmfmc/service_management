# Service Management API
API REST para la gestión operativa de un servicio técnico de electrodomésticos. El sistema permite administrar usuarios internos, clientes, direcciones, aparatos instalados, catálogos auxiliares, avisos de trabajo y cargos económicos asociados.

## Tabla de contenidos
- [Descripción funcional](#descripción-funcional)
- [Stack tecnológico](#stack-tecnológico)
- [Módulos principales](#módulos-principales)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Configuración](#configuración)
- [Puesta en marcha](#puesta-en-marcha)
- [Primer acceso y datos de prueba](#primer-acceso-y-datos-de-prueba)
- [Autenticación y autorización](#autenticación-y-autorización)
- [Formato de respuestas](#formato-de-respuestas)
- [Resumen de endpoints](#resumen-de-endpoints)
- [Documentación adicional](#documentación-adicional)
- [Licencia](#licencia)

## Descripción funcional
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
 **Java 21**.
- **Spring Boot 4.0.0**.
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
├── authentication/   # Login, JWT, filtro y utilidades de seguridad
├── config/           # Configuración de seguridad
├── controllers/      # Endpoints REST
├── errors/           # Excepciones de dominio y manejo global
├── models/           # Entidades, DTOs, enums y mappers
├── repositories/     # Repositorios JPA
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
- MySQL 8+ si ejecuta la base de datos fuera de contenedores.
- Docker + Docker Compose si prefiere usar el `docker-compose.yml` del repositorio.

## Configuración
La aplicación lee su configuración desde variables de entorno declaradas en `src/main/resources/application.properties`.

Variables requeridas:
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXP_MINUTES`

Variables opcionales para el primer acceso y los datos demo (solo si quiere cambiar los valores por defecto definidos en el backend):
- `APP_INITIAL_ADMIN_ENABLED`
- `APP_INITIAL_ADMIN_NAME`
- `APP_INITIAL_ADMIN_PHONE`
- `APP_INITIAL_ADMIN_USERNAME`
- `APP_INITIAL_ADMIN_PASSWORD`
- `APP_DEMO_DATA_ENABLED`

### Opción recomendada: archivo local no versionado
El repositorio incluye un ejemplo en la raíz:

```bash
cp application-local.example.properties src/main/resources/application-local.properties
```

Después, ajuste los valores a su entorno. Si quiere trabajar con un perfil local de Spring, puede usar ese archivo no versionado:

```text
src/main/resources/application-local.properties
```

Con el mismo conjunto de propiedades esperado por Spring:

```properties
DB_URL=jdbc:mysql://localhost:3306/service_management?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=root
JWT_SECRET=change_this_secret_key_with_at_least_32_chars
JWT_EXP_MINUTES=180
```

> El archivo de ejemplo versionado existente es `application-local.example.properties` y está situado en la raíz del proyecto.

### Opción alternativa: exportar variables directamente
```bash
export DB_URL='jdbc:mysql://localhost:3306/service_management?useSSL=false&serverTimezone=UTC'
export DB_USER='root'
export DB_PASSWORD='root'
export JWT_SECRET='change_this_secret_key_with_at_least_32_chars'
export JWT_EXP_MINUTES='180'
```

## Puesta en marcha
### Opción 1: base de datos local sin Docker
1. Cree una base de datos MySQL accesible desde su máquina.
2. Defina las variables de entorno requeridas.
3. Arranque la API:

```bash
./mvnw spring-boot:run
```

### Opción 2: MySQL con Docker Compose
El repositorio incluye `docker-compose.yml` para levantar una instancia local de MySQL 8.

1. Inicie la base de datos:

```bash
docker compose up -d
```

2. Exporte o cargue estas credenciales locales:

```bash
export DB_URL='jdbc:mysql://localhost:3306/service_management?useSSL=false&serverTimezone=UTC'
export DB_USER='root'
export DB_PASSWORD='root'
export JWT_SECRET='change_this_secret_key_with_at_least_32_chars'
export JWT_EXP_MINUTES='180'
```

3. Arranque la API:

```bash
./mvnw spring-boot:run
```

4. Detenga la base de datos cuando termine:

```bash
docker compose down
```

### Compilación y pruebas básicas
```bash
./mvnw clean verify
```

## Primer acceso y datos de prueba
Para resolver el problema del **primer acceso**, la aplicación puede crear automáticamente un usuario administrador si la base de datos arranca vacía.

### Comportamiento por defecto
- Si no existe ningún usuario, al arrancar se crea un admin inicial.
- Credenciales por defecto:
  - `username`: `admin`
  - `password`: `admin123456`
- Si ya existe al menos un usuario, no se crea nada.

### Variables de entorno relacionadas
Si quiere cambiar el comportamiento por defecto, puede definir por ejemplo:

```bash
export APP_INITIAL_ADMIN_ENABLED='true'
export APP_INITIAL_ADMIN_NAME='Administrador inicial'
export APP_INITIAL_ADMIN_PHONE='600000000'
export APP_INITIAL_ADMIN_USERNAME='admin'
export APP_INITIAL_ADMIN_PASSWORD='admin123456'
export APP_DEMO_DATA_ENABLED='false'
```

### Datos de demo opcionales
Si activa `APP_DEMO_DATA_ENABLED=true`, además del admin inicial se cargan catálogos básicos de prueba:
- marcas (`Balay`, `Bosch`, `Fagor`)
- tipos de aparato (`Lavadora`, `Frigorífico`, `Lavavajillas`)
- averías comunes asociadas a esos tipos

> Recomendación: cambie la contraseña por defecto del admin inicial en cuanto arranque el sistema por primera vez, especialmente fuera de local/desarrollo.

## Autenticación y autorización
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
  "password": "secret123"
}
```

### Uso del token
Envíe el token en la cabecera `Authorization`:

```http
Authorization: Bearer <jwt>
```

### Roles
- `ADMIN`: acceso total sobre usuarios, clientes, direcciones, electrodomésticos, catálogos, avisos y cargos.
- `TECH`: acceso a operaciones técnicas sobre órdenes asignadas y gestión parcial de cargos.

## Formato de respuestas
### Respuesta paginada
Los endpoints paginados devuelven un objeto con esta estructura:

```json
{
  "content": [],
  "currentPage": 0,
  "pageSize": 10,
  "totalItems": 0,
  "itemsInPage": 0,
  "totalPages": 0,
  "first": true,
  "last": true
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
- [Arquitectura](docs/ARCHITECTURE.md)
- [Modelo de datos](docs/DATA_MODEL.md)
- [Referencia de API](docs/API_REFERENCE.md)
- [Guía de uso del repositorio en GitHub](docs/GITHUB_SETUP.md)

## Licencia
Consulte el archivo [LICENSE](LICENSE).