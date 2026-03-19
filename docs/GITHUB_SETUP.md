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
Cree la base de datos manualmente y asegúrese de tener credenciales válidas.

### 3. Configurar la aplicación
La forma más clara es partir del ejemplo incluido:

```bash
copy "application-local.example.properties" "src/main/resources/application-local.properties"
```

> Al usar Docker Compose o al generar su archivo a partir del `.example`, asegúrese de mantener también la propiedad `spring.jpa.hibernate.ddl-auto=update` para que Hibernate cree y actualice las tablas automáticamente en local.


Si prefiere variables de entorno, defina:

```bash
export DB_URL='jdbc:mysql://localhost:3306/service_management?useSSL=false&serverTimezone=UTC'
export DB_USER='root'
export DB_PASSWORD='root'
export JWT_SECRET='change_this_secret_key_with_at_least_32_chars'
export JWT_EXP_MINUTES='180'
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

> Si lo configura en un archivo `.properties` use `spring.jpa.hibernate.ddl-auto`; si lo exporta como variable de entorno use `SPRING_JPA_HIBERNATE_DDL_AUTO`.

También puede añadir los datos del usuario administrador de prueba:

```bash
export APP_INITIAL_ADMIN_ENABLED='true'
export APP_INITIAL_ADMIN_NAME='Administrador inicial'
export APP_INITIAL_ADMIN_PHONE='600000000'
export APP_INITIAL_ADMIN_USERNAME='admin'
export APP_INITIAL_ADMIN_PASSWORD='admin123456'
export APP_DEMO_DATA_ENABLED='true'
```

> Si la base de datos está vacía, la aplicación crea automáticamente un administrador inicial. Si además activa `APP_DEMO_DATA_ENABLED=true`, también carga catálogos de ejemplo.

### 4. Ejecutar la aplicación
Para ejecutar la aplicación con la configuración del archivo local `application-local.properties`, ejecute:
```bash
.\mvnw spring-boot:run "-Dspring-boot.run.profiles=local"
```

En caso de no utilizar archivo local y utilizar solo `application.properties`, ejecute:
```bash
.\mvnw spring-boot:run
```


### 5. Ejecutar verificaciones
```bash
./mvnw test
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
