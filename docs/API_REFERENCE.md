# Referencia de API
## Convenciones generales
- **Base path**: `/api`.
- **Formato**: JSON.
- **Autenticación**: JWT en cabecera `Authorization: Bearer <token>`.
- **Paginación**: los listados paginados aceptan `page` y devuelven `PageResponse`.
- **Fechas**: los filtros por fecha usan formato `YYYY-MM-DD`.

## Autenticación
### `POST /auth/login`
**Acceso**: público.
**Body**
```json
{
  "username": "admin",
  "password": "secret123"
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
- `GET /work-orders/pending?page=0` filtra avisos pendientes.
- `GET /work-orders/user-scheduled/{userId}?date=YYYY-MM-DD&page=0` filtra por técnico y fecha.
- `GET /work-orders/created?date=YYYY-MM-DD&page=0` filtra por fecha de creación.

### Operaciones `ADMIN` y `TECH`
- `PUT /work-orders/tech/{id}` actualiza la parte técnica del aviso.
- `GET /work-orders/tech?date=YYYY-MM-DD&page=0` lista avisos técnicos del usuario autenticado.
- `GET /work-orders/tech/{id}` obtiene detalle técnico del aviso.

### Body de `WorkOrderFullRequestDto`
Este DTO combina varios bloques para crear o editar un aviso completo:

- `client`: alta embebida de cliente.
- `clientId`: reutiliza un cliente existente.
- `address`: alta embebida de dirección.
- `addressId`: reutiliza una dirección existente.
- `newAppliances`: lista de aparatos a crear en la dirección.
- `applianceIds`: conjunto de aparatos ya existentes para vincular al aviso.
- `workOrder`: bloque principal del aviso, obligatorio.
- `charges`: cargos iniciales opcionales.
- `tenant`: alta embebida de inquilino.
- `tenantId`: reutiliza un inquilino existente.

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
**Estados permitidos**:
- `NEW`
- `IN_PROGRESS`
- `PENDING_PART`
- `PENDING_CUSTOMER`
- `PENDING_PAYMENT`
- `APPLIANCE_INSTALLED`
- `CLOSED`

**Prioridades**:
- `1`: baja
- `2`: media
- `3`: alta
- `4`: urgente

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
- `chargeType`: obligatorio. Valores: `VISIT`, `REPAIR`, `INSTALLATION`, `NOT_SPECIFIED`, `OTHER`.
- `description`: opcional, máximo 45.
- `price`: obligatorio, mayor o igual que 0.
- `payer`: opcional, máximo 45.
- `paid`: opcional.
- `paymentMethod`: obligatorio. Valores: `CASH`, `CARD`, `BIZUM`, `TRANSFER`, `NOT_SPECIFIED`, `OTHER`.

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

### Errores
Errores típicos:
- `400 BAD_REQUEST`: validación, JSON inválido o parámetros incorrectos.
- `401 UNAUTHORIZED`: falta autenticación válida.
- `403 FORBIDDEN`: token válido, pero sin permisos.
- `404 NOT_FOUND`: recurso o endpoint inexistente.
- `405 METHOD_NOT_ALLOWED`: método HTTP no soportado.
- `409 CONFLICT`: recurso duplicado.