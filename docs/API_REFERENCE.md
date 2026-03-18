# Referencia de API

Base path: `/api`

## Autenticación

### POST `/auth/login`
- Acceso: Público.
- Body:
  - `username` (string, requerido)
  - `password` (string, requerido)
- Respuesta: token + `userId` + `username` + `role`.

---

## Usuarios (`/users`) — ADMIN
- `POST /users` crear usuario
- `PUT /users/{id}` actualizar datos (sin contraseña)
- `PUT /users/{id}/password` cambiar contraseña
- `GET /users` listar
- `GET /users/{id}` detalle
- `DELETE /users/{id}` eliminar

## Clientes (`/clients`) — ADMIN
- `POST /clients` crear
- `PUT /clients/{id}` actualizar
- `DELETE /clients/{id}` eliminar
- `GET /clients?page=0` listado paginado
- `GET /clients/{id}` detalle
- `GET /clients/search?q=texto` búsqueda

## Direcciones (`/addresses`) — ADMIN
- `POST /addresses/{clientId}` crear dirección para cliente
- `PUT /addresses/{id}/{clientId}` actualizar
- `DELETE /addresses/{id}` eliminar
- `GET /addresses?page=0` listado paginado
- `GET /addresses/{id}` detalle
- `GET /addresses/client/{clientId}` listado por cliente

## Electrodomésticos (`/appliances`) — ADMIN
- `POST /appliances/{addressId}` crear en dirección
- `PUT /appliances/{addressId}/{id}` actualizar
- `DELETE /appliances/{id}` desactivar
- `GET /appliances?page=0` listado paginado
- `GET /appliances/address/{addressId}` listado por dirección
- `GET /appliances/{id}` detalle

## Marcas (`/brands`) — ADMIN
- `POST /brands`
- `PUT /brands/{id}`
- `DELETE /brands/{id}`
- `GET /brands`
- `GET /brands/{id}`

## Tipos de aparato (`/appliance-types`) — ADMIN
- `POST /appliance-types`
- `PUT /appliance-types/{id}`
- `DELETE /appliance-types/{id}`
- `GET /appliance-types`
- `GET /appliance-types/{id}`

## Averías comunes (`/common-faults`) — ADMIN
- `POST /common-faults`
- `PUT /common-faults/{id}`
- `DELETE /common-faults/{id}`
- `GET /common-faults`
- `GET /common-faults/{id}`
- `GET /common-faults/appliance-type/{applianceTypeId}`

## Avisos (`/work-orders`)
### ADMIN
- `POST /work-orders` crear aviso completo
- `PUT /work-orders/{id}` actualizar aviso completo
- `DELETE /work-orders/{id}` eliminar
- `GET /work-orders/{id}` detalle
- `GET /work-orders?page=0` listado paginado
- `GET /work-orders/scheduled?date=YYYY-MM-DD&page=0`
- `GET /work-orders/pending?page=0`
- `GET /work-orders/user-scheduled/{userId}?date=YYYY-MM-DD&page=0`
- `GET /work-orders/created?date=YYYY-MM-DD&page=0`

### ADMIN y TECH
- `PUT /work-orders/tech/{id}` actualización técnica
- `GET /work-orders/tech?date=YYYY-MM-DD&page=0` listado técnico del usuario autenticado
- `GET /work-orders/tech/{id}` detalle técnico

## Cargos (`/charges`)
### ADMIN y TECH
- `POST /charges/work-order/{workOrderId}` crear cargo
- `PUT /charges/{id}` actualizar cargo
- `GET /charges/{id}` detalle
- `GET /charges/work-order/{workOrderId}` listado por aviso

### Sólo ADMIN
- `GET /charges?page=0` listado global paginado
- `DELETE /charges/{id}` eliminar cargo

---

## DTOs de entrada clave (resumen)

- `UserRequestDto`: nombre, teléfono, username, password, role.
- `ClientRequestDto`: nombre, teléfonos opcionales, email, notas.
- `AddressRequestDto`: dirección, ciudad, provincia, CP.
- `ApplianceRequestDto`: applianceTypeId, brandId opcional, modelo, serie, active.
- `WorkOrderRequestDto`: asignación, estado, prioridad, descripción, notas, trabajo realizado, descuento, facturar a, fecha programada.
- `WorkOrderFullRequestDto`: estructura compuesta para crear/actualizar aviso completo (cliente, dirección, aparatos, orden y cargos).
- `WorkOrderChargeRequestDto`: tipo de cargo, descripción, precio, pagador, pagado, método de pago.

Para validaciones concretas de longitud/obligatoriedad, consultar clases DTO en `models/dtos/requests`.