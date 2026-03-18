# Arquitectura del sistema

## Visión general
La aplicación sigue una arquitectura en capas centrada en una API REST con Spring Boot:

1. **Controllers**: exponen endpoints HTTP y delegan la lógica de negocio.
2. **Services**: aplican reglas de dominio, validaciones de negocio y composición de casos de uso.
3. **Repositories**: encapsulan acceso a datos con Spring Data JPA.
4. **Models / DTOs / Mappers**: representan persistencia, contratos de entrada/salida y transformaciones.
5. **Security layer**: autentica con JWT y autoriza por rol y ruta.
6. **Error handling**: normaliza excepciones y devuelve respuestas homogéneas.

## Flujo de una petición
1. El cliente envía una petición HTTP a un endpoint bajo `/api`.
2. Spring Security intercepta la petición.
3. Si el endpoint es protegido, `JwtAuthFilter` extrae y valida el token JWT.
4. El `Controller` recibe el request y, cuando procede, valida DTOs con anotaciones de Jakarta Validation.
5. El `Service` resuelve entidades, aplica reglas de negocio y coordina repositorios.
6. Los mappers convierten entidades a DTOs de respuesta.
7. La respuesta vuelve como JSON.
8. Si ocurre una excepción, `GlobalExceptionHandler` construye una respuesta de error consistente.

## Organización por paquetes
### `authentication`
Contiene el flujo de autenticación:
- `AuthController`: endpoint de login.
- `AuthService`: validación de credenciales y recuperación de usuario.
- `JwtService`: creación y lectura del token.
- `JwtAuthFilter`: inserta autenticación en el contexto de seguridad.
- `SecurityUtils`: utilidades para recuperar el usuario autenticado.

### `config`
- `SecurityConfig`: define política stateless, desactiva CSRF y configura permisos por endpoint.
- `InitialDataConfig`: crea el primer usuario administrador cuando no existen usuarios y, opcionalmente, carga catálogos demo.
- `AppStartupProperties`: concentra la configuración tipada de arranque (`initial-admin`, `demo-data`) y sus valores por defecto.

### `controllers`
Capa de entrada HTTP para cada agregado de negocio:
- usuarios
- clientes
- direcciones
- electrodomésticos
- catálogos
- avisos
- cargos

### `services`
Implementan la lógica principal del dominio. Destacan:
- composición de avisos completos;
- filtros paginados;
- actualización técnica de avisos;
- recálculo y persistencia de cargos;
- asociación entre cliente, dirección, aparatos e inquilino.

### `models`
Incluye:
- entidades JPA;
- DTOs de request;
- DTOs de response;
- DTOs ligeros para listados;
- enums del dominio;
- mappers.

### `repositories`
Repositorios JPA por entidad. Son la única capa que accede directamente a la base de datos.

### `errors`
Excepciones de negocio y handlers para:
- validaciones;
- autenticación;
- autorización;
- conflictos;
- recursos inexistentes;
- errores de formato JSON;
- parámetros obligatorios ausentes.

## Seguridad
### Enfoque
La seguridad es **stateless**: no hay sesión de servidor y cada request autenticado debe incluir JWT.

### Reglas principales
- `POST /api/auth/login`: público.
- `/api/users/**`: solo `ADMIN`.
- `/api/clients/**`: solo `ADMIN`.
- `/api/addresses/**`: solo `ADMIN`.
- `/api/appliances/**`: solo `ADMIN`.
- `/api/brands/**`: solo `ADMIN`.
- `/api/appliance-types/**`: solo `ADMIN`.
- `/api/common-faults/**`: solo `ADMIN`.
- `GET /api/charges`: solo `ADMIN`.
- `GET /api/charges/**`, `POST /api/charges/**`, `PUT /api/charges/**`: `ADMIN` y `TECH`.
- `DELETE /api/charges/**`: solo `ADMIN`.
- `/api/work-orders/tech/**`: `ADMIN` y `TECH`.
- resto de `/api/work-orders/**`: solo `ADMIN`.

### Consecuencias de diseño
- Los técnicos no pueden crear clientes, direcciones o aparatos.
- Los técnicos sí pueden trabajar sobre su vista técnica de avisos.
- Los cargos tienen un acceso mixto: operación diaria por técnicos, supervisión global y borrado solo por administración.

## Modelo de interacción de avisos
El agregado `WorkOrder` es el núcleo del sistema y conecta varias piezas:

- cliente principal;
- dirección de servicio;
- aparatos asociados;
- usuario asignado;
- usuario creador y último editor;
- inquilino opcional;
- cargos económicos.

Esto explica por qué existe un DTO compuesto (`WorkOrderFullRequestDto`): el backend permite crear o actualizar un aviso completo en una sola operación.

## Persistencia y relaciones
- **Client → Address**: uno a muchos.
- **Address → Appliance**: uno a muchos.
- **WorkOrder → Appliance**: muchos a muchos.
- **WorkOrder → WorkOrderCharge**: uno a muchos con `cascade = ALL` y `orphanRemoval = true`.
- **WorkOrder → User / Client / Address**: varias relaciones muchos a uno.

## Auditoría automática
Se usa auditoría ligera mediante callbacks JPA:
- `User`: `createdAt` y `lastUpdatedAt`.
- `Client`: `createdAt`.
- `WorkOrder`: `createdAt` y `lastUpdatedAt`, además de `closedAt` cuando corresponde a la lógica de negocio.
- `WorkOrderCharge`: `createdAt`.

## Paginación
Los listados paginados se transforman a un contrato común `PageResponse`, lo que evita devolver directamente la estructura interna de Spring Data.

Campos relevantes:
- `content`
- `currentPage`
- `pageSize`
- `totalItems`
- `itemsInPage`
- `totalPages`
- `first`
- `last`

## Manejo de errores
La API centraliza errores en `GlobalExceptionHandler` para mantener respuestas coherentes.

### Casos cubiertos
- DTO inválido (`MethodArgumentNotValidException`).
- JSON mal formado o enums inválidos (`HttpMessageNotReadableException`).
- recursos duplicados (`AlreadyExistsException`).
- recursos no encontrados (`NotFoundException`).
- acceso no autenticado (`UnauthorizedException`).
- acceso prohibido (`ForbiddenException`).
- errores de negocio (`BadRequestException`, `IllegalArgumentException`).
- parámetro obligatorio ausente.
- método HTTP no permitido.

## Primer acceso y datos demo
- Si la base de datos no contiene usuarios, el backend crea automáticamente un administrador inicial configurable mediante propiedades tipadas.
- Si ya hay usuarios o catálogos creados, no los duplica.
- Puede cargar datos demo de catálogos mediante `app.demo-data.enabled`, útil para entornos de desarrollo.

## Decisiones de diseño visibles en el código
- **Soft delete de aparatos**: `DELETE /api/appliances/{id}` desactiva el registro en lugar de eliminarlo físicamente.
- **Composición rica de DTOs**: las respuestas detalladas agregan información de cliente, dirección, aparatos y cargos para reducir round-trips del frontend.
- **Enums del dominio con etiquetas en español**: el backend maneja valores estables en mayúsculas y conserva etiquetas legibles para presentación