# Arquitectura del sistema

## Visión general
La aplicación sigue una arquitectura por capas típica en Spring:

1. **Controller**: expone endpoints REST y valida payloads de entrada.
2. **Service**: implementa reglas de negocio y orquestación.
3. **Repository**: acceso a datos con Spring Data JPA.
4. **Model/DTO/Mapper**: entidades de persistencia, contratos de API y transformaciones.

## Flujo de una petición
1. Llega una petición HTTP al controller.
2. Spring valida el DTO con anotaciones (`@NotNull`, `@Size`, etc.).
3. El service aplica reglas de dominio y consulta repositorios.
4. Los mappers convierten entidades a DTOs de salida.
5. Se devuelve respuesta JSON.
6. En caso de error, `GlobalExceptionHandler` transforma la excepción a respuesta REST.

## Seguridad
Componentes principales:
- `authentication/AuthController`: login.
- `authentication/AuthService`: validación de credenciales.
- `authentication/JwtService`: generación y parsing de tokens.
- `authentication/JwtAuthFilter`: extrae JWT de cabecera y crea autenticación.
- `config/SecurityConfig`: define permisos por endpoint/método.

Características:
- Sesión stateless (`SessionCreationPolicy.STATELESS`).
- CSRF desactivado (API token-based).
- Handlers personalizados para no autenticado y sin permisos.

## Paquetes principales
- `controllers`: APIs de negocio.
- `services`: lógica por agregado (users, clients, work-orders, etc.).
- `repositories`: repositorios por entidad.
- `models`: entidades JPA y DTOs.
- `models/enums`: catálogos cerrados (roles, estados, prioridades, etc.).
- `errors`: excepciones de negocio y manejo centralizado.

## Paginación
Los listados paginados usan un DTO común (`PageResponse`) y un mapper (`PageMapper`) para exponer metadatos de página de forma consistente.

## Estrategia de soft/hard delete
Depende del recurso:
- Algunos recursos se eliminan físicamente (delete tradicional).
- En electrodomésticos se maneja desactivación (`active`) para conservar historial.