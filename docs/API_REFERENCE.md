# Referencia de API
Guía operativa de los endpoints expuestos por la aplicación.

## Tabla de contenidos
- [Convenciones generales](#convenciones-generales)
- [Autenticación](#autenticación)
- [Usuarios (`/users`) — solo `ADMIN`](#usuarios-users--solo-admin)
- [Clientes (`/clients`) — solo `ADMIN`](#clientes-clients--solo-admin)
- [Direcciones (`/addresses`) — solo `ADMIN`](#direcciones-addresses--solo-admin)
- [Electrodomésticos (`/appliances`) — solo `ADMIN`](#electrodomésticos-appliances--solo-admin)
- [Marcas (`/brands`) — solo `ADMIN`](#marcas-brands--solo-admin)
- [Tipos de aparato (`/appliance-types`) — solo `ADMIN`](#tipos-de-aparato-appliance-types--solo-admin)
- [Averías comunes (`/common-faults`) — solo `ADMIN`](#averías-comunes-common-faults--solo-admin)
- [Avisos (`/work-orders`)](#avisos-work-orders)
- [Cargos (`/charges`)](#cargos-charges)
- [Respuestas y errores](#respuestas-y-errores)

## Convenciones generales
- **Base path**: `/api`.
- **Formato**: JSON.
- **Autenticación**: JWT en cabecera `Authorization: Bearer <token>`.
- **Paginación**: los listados paginados aceptan `page` y devuelven `PageResponse`.
- **Fechas**: los filtros por fecha usan formato `YYYY-MM-DD`.
- **Roles**: si no se indica lo contrario, los endpoints protegidos requieren autenticación.

## Autenticación
### `POST /auth/login`
**Acceso**: público.
**Body**
```json
{
  "username": "admin",
  "password": "admin123456"
}
```
**Validaciones**
- `username`: obligatorio, entre 3 y 20 caracteres.
- `password`: obligatoria, mínimo 6 caracteres.
**Respuesta**
```json
{
  "token": "<jwt>",
  "userId": 1,
  "username": "admin",
  "role": "ADMIN"
}
```

---

## Usuarios (`/users`) — solo `ADMIN`
### Endpoints
- `POST /users` crea un usuario.
- `PUT /users/{id}` actualiza datos sin cambiar contraseña.
- `PUT /users/{id}/password` cambia la contraseña.
- `GET /users` lista todos los usuarios.
- `GET /users/{id}` obtiene detalle.
- `DELETE /users/{id}` elimina un usuario.

### `POST /users`
**Body**
- `name`: obligatorio, máximo 45.
- `phone`: obligatorio, entre 9 y 12.
- `username`: obligatorio, entre 3 y 20.
- `password`: obligatoria, mínimo 6.
- `role`: obligatorio (`ADMIN`, `TECH`).

### `PUT /users/{id}`
**Body**
- `name`: obligatorio, máximo 45.
- `phone`: obligatorio, entre 9 y 12.
- `username`: obligatorio, entre 3 y 20.
- `active`: obligatorio.
- `role`: obligatorio (`ADMIN`, `TECH`).

### `PUT /users/{id}/password`
**Body**
- `password`: obligatoria, mínimo 6.

---

## Clientes (`/clients`) — solo `ADMIN`
### Endpoints
- `POST /clients` crea un cliente.
- `PUT /clients/{id}` actualiza un cliente.
- `DELETE /clients/{id}` elimina un cliente.
- `GET /clients?page=0` lista clientes paginados.
- `GET /clients/{id}` obtiene detalle.
- `GET /clients/search?q=texto` busca por texto.

### Body de cliente
- `name`: obligatorio, máximo 50.
- `phone`: obligatorio, entre 9 y 12.
- `phone2`, `phone3`, `phone4`: opcionales, entre 9 y 12.
- `email`: opcional, válido, máximo 50.
- `notes`: opcional, máximo 100.

### Respuesta paginada de `GET /clients`
Cada elemento de `content` es un `ClientListDto` con:
- `id`
- `name`
- `phone`
- `phone2`
- `addressesNames`
- `addressesCities`

---

## Direcciones (`/addresses`) — solo `ADMIN`
### Endpoints
- `POST /addresses/{clientId}` crea una dirección para un cliente.
- `PUT /addresses/{id}/{clientId}` actualiza una dirección y su cliente asociado.
- `DELETE /addresses/{id}` elimina una dirección.
- `GET /addresses?page=0` lista direcciones paginadas.
- `GET /addresses/{id}` obtiene detalle.
- `GET /addresses/client/{clientId}` lista direcciones de un cliente.

### Body de dirección
- `address`: obligatorio, máximo 100.
- `city`: opcional, máximo 45.
- `province`: opcional, máximo 45.
- `postalCode`: opcional, máximo 20.

---

## Electrodomésticos (`/appliances`) — solo `ADMIN`
### Endpoints
- `POST /appliances/{addressId}` crea un aparato en una dirección.
- `PUT /appliances/{addressId}/{id}` actualiza un aparato.
- `DELETE /appliances/{id}` lo desactiva (`active = false`).
- `GET /appliances?page=0` lista aparatos paginados.
- `GET /appliances/address/{addressId}` lista aparatos por dirección.
- `GET /appliances/{id}` obtiene detalle.

### Body de electrodoméstico
- `applianceTypeId`: obligatorio, positivo.
- `brandId`: opcional, positivo.
- `model`: opcional, máximo 20.
- `serialNumber`: opcional, máximo 45.
- `active`: opcional.

---

## Marcas (`/brands`) — solo `ADMIN`
- `POST /brands`
- `PUT /brands/{id}`
- `DELETE /brands/{id}`
- `GET /brands`
- `GET /brands/{id}`

**Body**
- DTO simple de marca.
- El recurso se usa como catálogo maestro para aparatos.

## Tipos de aparato (`/appliance-types`) — solo `ADMIN`
- `POST /appliance-types`
- `PUT /appliance-types/{id}`
- `DELETE /appliance-types/{id}`
- `GET /appliance-types`
- `GET /appliance-types/{id}`

**Body**
- DTO simple de tipo de aparato.

## Averías comunes (`/common-faults`) — solo `ADMIN`
- `POST /common-faults`
- `PUT /common-faults/{id}`
- `DELETE /common-faults/{id}`
- `GET /common-faults`
- `GET /common-faults/{id}`
- `GET /common-faults/appliance-type/{applianceTypeId}`

**Body**
- DTO simple de avería común, vinculada a `applianceTypeId`.

---

## Avisos (`/work-orders`)
### Operaciones `ADMIN`
- `POST /work-orders` crea un aviso completo.
- `PUT /work-orders/{id}` actualiza un aviso completo.
- `DELETE /work-orders/{id}` elimina un aviso.
- `GET /work-orders/{id}` obtiene detalle administrativo.
- `GET /work-orders?page=0` lista todos los avisos paginados.
- `GET /work-orders/scheduled?date=YYYY-MM-DD&page=0` filtra por fecha programada.
- `GET /work-orders/pending?page=0` devuelve avisos con estado `PENDING_PART`, `PENDING_CUSTOMER` o `PENDING_PAYMENT`.
- `GET /work-orders/user-scheduled/{userId}?date=YYYY-MM-DD&page=0` filtra por técnico y fecha.
- `GET /work-orders/created?date=YYYY-MM-DD&page=0` filtra por fecha de creación.

### Operaciones `ADMIN` y `TECH`
- `PUT /work-orders/tech/{id}` actualiza la parte técnica del aviso.
- `GET /work-orders/tech?date=YYYY-MM-DD&page=0` lista avisos técnicos del usuario autenticado en la fecha programada.
- `GET /work-orders/tech/{id}` obtiene detalle técnico del aviso.

### Body de `WorkOrderFullRequestDto`
Este DTO permite crear o actualizar un aviso completo en una sola operación. Puede mezclar referencias existentes con creaciones embebidas.

- `client`: alta embebida de cliente.
- `clientId`: reutiliza un cliente existente.
- `address`: alta embebida de dirección.
- `addressId`: reutiliza una dirección existente.
- `newAppliances`: lista de aparatos a crear en la dirección.
- `applianceIds`: aparatos existentes que se quieren vincular al aviso.
- `workOrder`: bloque principal del aviso, obligatorio.
- `charges`: cargos iniciales opcionales.
- `tenant`: alta embebida de inquilino.
- `tenantId`: reutiliza un inquilino existente.

### Reglas prácticas para construir el request
- Debe enviar siempre `workOrder`.
- Puede usar `client` o `clientId` según quiera crear o reutilizar el cliente.
- Puede usar `address` o `addressId` con la misma lógica.
- `scheduledAt` es obligatoria y usa formato `YYYY-MM-DD`.

### Body de `workOrder`
- `assignedUserId`: opcional, positivo.
- `issueDescription`: opcional, máximo 100.
- `status`: opcional, enum `WorkOrderStatus`.
- `priority`: opcional, entero entre 1 y 4.
- `notes`: opcional, máximo 250.
- `workPerformed`: opcional, máximo 100.
- `discountVisit`: opcional.
- `billTo`: opcional, máximo 45.
- `scheduledAt`: obligatorio.

### Body de `PUT /work-orders/tech/{id}`
- `status`: opcional.
- `notes`: opcional, máximo 250.
- `workPerformed`: opcional, máximo 100.
- `billTo`: opcional, máximo 45.

### Estados y prioridad
**Estados**:
- `NEW`: Nuevo
- `IN_PROGRESS`: En progreso
- `PENDING_PART`: Pendiente de pieza
- `PENDING_CUSTOMER`: Pendiente del cliente
- `PENDING_PAYMENT`: Pendiente de pago
- `APPLIANCE_INSTALLED`: Aparato instalado
- `CLOSED`: Terminado

**Prioridades**:
- `1`: baja
- `2`: media
- `3`: alta
- `4`: urgente

### Ejemplo mínimo de creación de aviso
```json
{
  "clientId": 12,
  "addressId": 30,
  "applianceIds": [5],
  "workOrder": {
    "assignedUserId": 3,
    "issueDescription": "No enfría",
    "priority": 3,
    "scheduledAt": "2026-03-20"
  }
}
```

---

## Cargos (`/charges`)
### Operaciones `ADMIN` y `TECH`
- `POST /charges/work-order/{workOrderId}` crea un cargo.
- `PUT /charges/{id}` actualiza un cargo.
- `GET /charges/{id}` obtiene detalle.
- `GET /charges/work-order/{workOrderId}` lista cargos de un aviso.

### Operaciones solo `ADMIN`
- `GET /charges?page=0` lista global paginada.
- `DELETE /charges/{id}` elimina un cargo.

### Body de cargo
- `chargeType`: obligatorio
- `description`: opcional, máximo 45.
- `price`: obligatorio, mayor o igual que 0.
- `payer`: opcional, máximo 45.
- `paid`: opcional.
- `paymentMethod`: obligatorio.

### Tipo de cargo y método de pago
**Tipos de cargo / chargeType**:
- `VISIT`: Visita
- `REPAIR`: Reparación
- `INSTALLATION`: Instalación
- `NOT_SPECIFIED`: No especificado
- `OTHER`: Otro

**Métodos de pago / paymentMethod**:
- `CASH`: Efectivo
- `CARD`: Tarjeta
- `BIZUM`: Bizum
- `TRANSFER`: Transferencia
- `NOT_SPECIFIED`: No especificado
- `OTHER`: Otro

### Ejemplo
```json
{
  "chargeType": "REPAIR",
  "description": "Cambio de bomba",
  "price": 89.50,
  "payer": "Cliente final",
  "paid": false,
  "paymentMethod": "CARD"
}
```

---

## Respuestas y errores
### `PageResponse`
Los endpoints paginados comparten este contrato:

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

### Error de validación
```json
{
  "error": "BAD_REQUEST",
  "message": "La validación del request falló",
  "path": "/api/work-orders",
  "status": 400,
  "timestamp": "2026-03-19T12:00:00Z",
  "errors": {
    "scheduledAt": "la fecha programada es obligatoria"
  }
}
```

### Errores comunes
- `400 BAD_REQUEST`: validación, JSON inválido o parámetros obligatorios ausentes.
- `401 UNAUTHORIZED`: falta autenticación o el token no es válido.
- `403 FORBIDDEN`: el usuario autenticado no tiene permisos.
- `404 NOT_FOUND`: recurso o endpoint inexistente.
- `405 METHOD_NOT_ALLOWED`: método HTTP no permitido para la ruta.
- `409 CONFLICT`: recurso duplicado.