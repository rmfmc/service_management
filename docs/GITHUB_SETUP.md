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
Cree manualmente la base de datos y las credenciales necesarias.

### 3. Configurar variables de entorno
Defina:

```bash
export DB_URL='jdbc:mysql://localhost:3306/service_management?useSSL=false&serverTimezone=UTC'
export DB_USER='root'
export DB_PASSWORD='root'
export JWT_SECRET='change_this_secret_key_with_at_least_32_chars'
export JWT_EXP_MINUTES='180'
```

Si quiere cambiar el usuario inicial o activar datos demo, puede añadir también overrides como:

```bash
export APP_INITIAL_ADMIN_USERNAME='admin'
export APP_INITIAL_ADMIN_PASSWORD='admin123456'
export APP_DEMO_DATA_ENABLED='true'
```

### 4. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

En el primer arranque sobre una base de datos vacía, la aplicación crea automáticamente un usuario admin inicial. Si además activa `APP_DEMO_DATA_ENABLED=true`, también carga catálogos de ejemplo.

### 5. Ejecutar verificaciones
```bash
./mvnw test
```

## Recomendaciones para publicar en GitHub
- Añadir una descripción más específica en `pom.xml` si el proyecto va a ser público.
- Mantener un ejemplo de variables de entorno actualizado.
- Evitar subir secretos reales o ficheros locales con credenciales.
- Acompañar cambios de API con actualización simultánea de `README.md` y `docs/API_REFERENCE.md`.
- Añadir ejemplos de requests/responses cuando se introduzcan endpoints nuevos.

## Issues y Pull Requests
Buenas prácticas recomendadas:
- Crear una rama por cambio funcional.
- Mantener commits pequeños y descriptivos.
- Explicar en el PR si se han modificado contratos de API, seguridad o modelo de datos.
- Incluir comandos de validación ejecutados en local.

## Versionado de la documentación
Cuando cambie alguno de estos aspectos, revise la documentación correspondiente:

- **nuevos endpoints o cambios de contrato** → `docs/API_REFERENCE.md`
- **cambios estructurales del backend** → `docs/ARCHITECTURE.md`
- **nuevas entidades o relaciones** → `docs/DATA_MODEL.md`
- **cambios de arranque o configuración** → `README.md`