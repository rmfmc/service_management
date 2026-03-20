# Guía de uso del repositorio en GitHub
## Objetivo
Este documento describe una forma clara de trabajar con el repositorio cuando se publique o se utilice desde GitHub.

## Contenido recomendado del repositorio
Para que el proyecto sea fácil de consumir desde GitHub, conviene mantener actualizados estos elementos:

- `README.md`: visión general, arranque y enlaces a la documentación técnica.
- `docs/API_REFERENCE.md`: referencia operativa de endpoints.
- `docs/ARCHITECTURE.md`: visión interna de diseño.
- `docs/DATA_MODEL.md`: entidades y relaciones.
- `LICENSE`: licencia del proyecto.
- `application-local.example.properties`: ejemplo de configuración local.

## Flujo de trabajo sugerido
### 1. Clonar el repositorio
```bash
git clone <repo-url>
cd service_management
```

### 2. Preparar la base de datos
Puede hacerlo de dos maneras:

#### Opción A: Docker Compose
```bash
docker compose up -d
```

#### Opción B: MySQL local
Cree manualmente una base de datos llamada `service_management` y asegúrese de disponer de credenciales válidas.

### 3. Configurar la aplicación
La forma recomendada es partir del archivo de ejemplo incluido y crear su configuración local:
```bash
copy "application-local.example.properties" "src/main/resources/application-local.properties"
```

Revise después el contenido del archivo y ajuste los valores si lo necesita. Un ejemplo válido sería:
```properties
DB_URL=jdbc:mysql://localhost:3306/service_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=root
JWT_SECRET=change_this_secret_key_with_at_least_32_chars
JWT_EXP_MINUTES=180
spring.jpa.hibernate.ddl-auto=update
```

> Se recomienda mantener `spring.jpa.hibernate.ddl-auto=update` en desarrollo local para que Hibernate cree y actualice automáticamente las tablas.

### 4. Ejecutar la aplicación
Para trabajar con `application-local.properties`, ejecute la aplicación con el perfil local.
```bash
.\mvnw spring-boot:run "-Dspring-boot.run.profiles=local"
```

> En caso de no utilizar archivo local y utilizar solo `application.properties`, ejecute: `.\mvnw spring-boot:run`.

### 5. Ejecutar verificaciones
```bash
.\mvnw test
```

## Recomendaciones para mantener la documentación clara
- Actualice `README.md` cuando cambie el arranque, la configuración o el flujo básico de uso.
- Actualice `docs/API_REFERENCE.md` cuando cambien endpoints, validaciones o contratos de request/response.
- Actualice `docs/ARCHITECTURE.md` cuando cambien seguridad, capas o decisiones de diseño.
- Actualice `docs/DATA_MODEL.md` cuando cambien entidades, relaciones o enums.
- Añada ejemplos de request/response cuando un endpoint nuevo no sea obvio a primera vista.

## Issues y Pull Requests
- Use una rama por cambio funcional.
- Mantenga commits pequeños y descriptivos.
- Explique en el PR si se han modificado contratos de API, seguridad o modelo de datos.
- Anote los comandos de validación ejecutados.
- Si cambia documentación por una decisión de código, enlace ambas cosas en la descripción del PR.
