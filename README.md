# Service Management API

Backend REST para la gestión de avisos técnicos, clientes, domicilios, electrodomésticos y cobros.

## Tabla de contenidos
- [Descripción](#descripción)
- [Características](#características)
- [Stack tecnológico](#stack-tecnológico)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Configuración de entorno](#configuración-de-entorno)
- [Arranque local](#arranque-local)
- [Autenticación y roles](#autenticación-y-roles)
- [Resumen de endpoints](#resumen-de-endpoints)
- [Documentación detallada](#documentación-detallada)
- [Licencia](#licencia)

## Descripción
El proyecto implementa una API basada en **Spring Boot + JPA + MySQL** para administrar el ciclo completo de trabajo de un servicio técnico:

- Gestión de usuarios internos (administradores y técnicos).
- Gestión de clientes y direcciones.
- Gestión de electrodomésticos por dirección.
- Catálogos de apoyo (marcas, tipos de aparato, averías comunes).
- Gestión de avisos/órdenes de trabajo.
- Registro de cargos y cobros asociados a cada aviso.
- Seguridad con JWT y autorización por rol.

## Características
- API REST con validación de entrada (`jakarta.validation`).
- Autenticación stateless por JWT.
- Reglas de autorización por endpoint y método HTTP.
- Persistencia con Spring Data JPA.
- Soporte de paginación en listados principales.
- Manejo global de excepciones y respuestas de error homogéneas.

## Stack tecnológico
- **Java 21**
- **Spring Boot 4.0.0**
- Spring Web MVC
- Spring Security
- Spring Data JPA
- MySQL Connector/J
- JSON Web Tokens (JJWT 0.13.0)
- Maven

## Estructura del proyecto
```text
src/main/java/ruben/springboot/service_management
├── authentication/   # Login, JWT, filtro y utilidades de seguridad
├── config/           # Configuración de seguridad
├── controllers/      # Endpoints REST
├── errors/           # Excepciones y handlers globales
├── models/           # Entidades, DTOs, enums y mappers
├── repositories/     # Repositorios JPA
└── services/         # Lógica de negocio
```

## Requisitos
- JDK 21 o superior.
- Maven 3.9+ o uso de `./mvnw`.
- MySQL 8+ si desea ejecutar la base de datos sin contenedores.
- Docker y Docker Compose si prefiere levantar MySQL con contenedores.

## Configuración de entorno
La configuración sensible y local del proyecto **no se sube al repositorio**.

La configuración base de la aplicación está en `src/main/resources/application.properties` y resuelve estos valores desde variables de entorno:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXP_MINUTES`

Además, puede trabajar con un perfil local de Spring usando un archivo no versionado:

```text
src/main/resources/application-local.properties
```

### Opción recomendada: usar `application-local.properties`
Copie el ejemplo versionado y renómbrelo:

```bash
cp src/main/resources/application-local.example.properties src/main/resources/application-local.properties
```

Después, ajuste los valores a su entorno local.

> `src/main/resources/application-local.properties` está ignorado en `.gitignore`, por lo que no se subirá al repositorio.


### Alternativa: variables de entorno sin archivo local
Si no quiere usar `application-local.properties`, también puede ejecutar la aplicación exportando las variables esperadas por `application.properties`:

```bash
export DB_URL='jdbc:mysql://localhost:3306/service_management?createDatabaseIfNotExist=true&serverTimezone=UTC'
export DB_USER='service_management'
export DB_PASSWORD='service_management'
export JWT_SECRET='cambia-esta-clave-por-una-larga-y-segura'
export JWT_EXP_MINUTES='60'
```

## Arranque local

### Opción 1: arranque sin Docker
Use esta opción si ya tiene MySQL instalado en su máquina.

1. Cree una base de datos MySQL y un usuario con permisos.
2. Copie `application-local.example.properties` a `application-local.properties`.
3. Revise la URL, credenciales y secreto JWT.
4. Arranque la aplicación con el perfil `local`.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

También puede arrancar sin perfil `local` si define las variables de entorno que consume `application.properties`:

```bash
./mvnw spring-boot:run
```

### Opción 2: arranque con Docker para MySQL
El repositorio incluye un `compose.yaml` para levantar una instancia local de MySQL.

1. Levante la base de datos:

```bash
docker compose up -d
```

2. Copie el archivo de ejemplo local:

```bash
cp application-local.example.properties src/main/resources/application-local.properties
```

3. Verifique que `application-local.properties` use `localhost:3306` y las credenciales del contenedor.
4. Arranque la API con el perfil `local`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

5. Para parar la base de datos:

```bash
docker compose down
```

### Build del proyecto
Para generar el artefacto del proyecto:

```bash
./mvnw clean package
```

## Autenticación y roles
### Login
- Endpoint público: `POST /api/auth/login`
- Recibe usuario y contraseña.
- Respuesta: token JWT + datos básicos del usuario autenticado.

### Uso del token
Envíe el token en la cabecera HTTP:

```http
Authorization: Bearer <token>
```

### Roles
- `ADMIN`: acceso total sobre usuarios, clientes, direcciones, electrodomésticos, catálogos y órdenes de trabajo.
- `TECH`: acceso a operaciones técnicas sobre órdenes asignadas y gestión parcial de cargos.

Detalle completo de permisos en `config/SecurityConfig.java`.

## Resumen de endpoints
> Referencia completa en `docs/API_REFERENCE.md`.

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

## Documentación detallada
- [Arquitectura](docs/ARCHITECTURE.md)
- [Modelo de datos](docs/DATA_MODEL.md)
- [Referencia de API](docs/API_REFERENCE.md)
- [Guía para GitHub](docs/GITHUB_SETUP.md)

## Licencia
Ver [LICENSE](LICENSE).