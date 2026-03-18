# Modelo de datos (resumen)

## Entidades principales

### User (`users`)
- Identidad y acceso de personal interno.
- Campos clave: `name`, `phone`, `username`, `passwordHash`, `role`, `active`.
- Auditoría: `createdAt`, `lastUpdatedAt`.

### Client (`clients`)
- Cliente final.
- Campos: `name`, `phone`, `phone2..4`, `email`, `notes`, `createdAt`.
- Relación: 1:N con `Address`.

### Address (`addresses`)
- Dirección asociada a un cliente.
- Campos típicos: `address`, `city`, `province`, `postalCode`.
- Relación: N:1 con `Client`.
- Relación: 1:N con `Appliance`.

### Appliance (`appliances`)
- Equipo instalado en una dirección.
- Campos: tipo, marca, modelo, serie, `active`.
- Relación N:1 con `Address`.
- Relación con catálogos `Brand` y `ApplianceType`.

### WorkOrder (`work_orders`)
- Aviso/orden principal de trabajo.
- Campos relevantes: técnico asignado, estado, prioridad, descripción avería,
  notas, trabajo realizado, fecha programada, descuentos, total, facturación.
- Relación con cliente principal y opcionalmente inquilino.
- Relación con `WorkOrderCharge` (1:N).

### WorkOrderCharge (`work_order_charges`)
- Cargos económicos de una orden.
- Campos: tipo de cargo, descripción, precio, pagador, pagado, método de pago.
- Auditoría de creación: usuario creador + fecha de alta.

### Catálogos
- `Brand`
- `ApplianceType`
- `CommonFault` (vinculada a tipo de aparato)

## Enumerados
- `UserRole`: `ADMIN`, `TECH`
- `WorkOrderStatus`: `NEW`, `IN_PROGRESS`, `PENDING_PART`, `PENDING_CUSTOMER`, `PENDING_PAYMENT`, `APPLIANCE_INSTALLED`, `CLOSED`
- `WorkOrderPriority`: 1-4 (`LOW`, `MID`, `HIGH`, `URGENT`)
- `ChargeType`: `VISIT`, `REPAIR`, `INSTALLATION`, `NOT_SPECIFIED`, `OTHER`
- `PaymentMethod`: `CASH`, `CARD`, `BIZUM`, `TRANSFER`, `NOT_SPECIFIED`, `OTHER`

## Auditoría automática
Varias entidades usan callbacks JPA (`@PrePersist`, `@PreUpdate`) para sellar fechas de creación/actualización automáticamente.