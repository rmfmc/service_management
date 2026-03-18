# Modelo de datos
## Visión general
El dominio gira alrededor del ciclo de trabajo de un servicio técnico. La entidad principal es `WorkOrder`, que enlaza cliente, dirección, aparatos, técnico asignado y cargos.

## Entidades principales
### `User` (`users`)
Representa al personal interno autenticable.

**Campos relevantes**
- `id`
- `name`
- `phone`
- `username`
- `passwordHash`
- `role`
- `active`
- `createdAt`
- `lastUpdatedAt`

**Notas**
- Solo existen los roles `ADMIN` y `TECH`.
- La contraseña no se expone en DTOs; se almacena como hash.

---

### `Client` (`clients`)
Representa al cliente final o, en algunos casos, al inquilino relacionado con un aviso.

**Campos relevantes**
- `id`
- `name`
- `phone`
- `phone2`
- `phone3`
- `phone4`
- `email`
- `notes`
- `createdAt`

**Relaciones**
- `1:N` con `Address`.

---

### `Address` (`addresses`)
Dirección física asociada a un cliente.

**Campos relevantes**
- `id`
- `client`
- `address`
- `city`
- `province`
- `postalCode`

**Relaciones**
- `N:1` con `Client`.
- `1:N` con `Appliance`.

---

### `Appliance` (`appliances`)
Aparato instalado en una dirección.

**Campos relevantes**
- `id`
- `address`
- `applianceType`
- `brand`
- `model`
- `serialNumber`
- `active`

**Relaciones**
- `N:1` con `Address`.
- `N:1` con `ApplianceType`.
- `N:1` con `Brand`.
- `N:M` con `WorkOrder`.

**Notas**
- El borrado funcional se implementa mediante desactivación (`active = false`).

---

### `Brand` (`brands`)
Catálogo maestro de marcas de aparatos.

**Campos relevantes**
- `id`
- `name`

---

### `ApplianceType` (`appliance_types`)
Catálogo maestro de tipos de aparato.

**Campos relevantes**
- `id`
- `name`

---

### `CommonFault` (`common_faults`)
Catálogo de averías frecuentes asociado a un tipo de aparato.

**Campos relevantes**
- `id`
- `applianceType`
- `name`

**Relaciones**
- `N:1` con `ApplianceType`.

---

### `WorkOrder` (`work_orders`)
Agregado central del sistema. Representa un aviso o una orden de trabajo.

**Campos relevantes**
- `id`
- `assignedUser`
- `createdUser`
- `lastUpdatedUser`
- `closedUser`
- `client`
- `address`
- `appliances`
- `issueDescription`
- `status`
- `priority`
- `notes`
- `workPerformed`
- `charges`
- `discountVisit`
- `totalPrice`
- `billTo`
- `scheduledAt`
- `createdAt`
- `closedAt`
- `lastUpdatedAt`
- `tenant`

**Relaciones**
- `N:1` con `User` para usuario asignado.
- `N:1` con `User` para creador y último editor.
- `N:1` con `User` para cierre opcional.
- `N:1` con `Client` para cliente principal.
- `N:1` con `Client` para inquilino opcional.
- `N:1` con `Address`.
- `N:M` con `Appliance`.
- `1:N` con `WorkOrderCharge`.

**Notas**
- La prioridad se guarda como entero (`1..4`) y se presenta con etiqueta en español.
- El estado se almacena como enum.
- El total económico se consolida en `totalPrice`.

---

### `WorkOrderCharge` (`work_order_charges`)
Movimiento económico asociado a un aviso.

**Campos relevantes**
- `id`
- `workOrder`
- `createdUserId`
- `createdUserName`
- `chargeType`
- `description`
- `price`
- `payer`
- `paid`
- `paymentMethod`
- `createdAt`

**Relaciones**
- `N:1` con `WorkOrder`.

**Notas**
- Guarda el identificador y nombre del usuario creador como snapshot de auditoría.

## Enumerados del dominio

### `UserRole`
- `ADMIN`
- `TECH`

### `WorkOrderStatus`
- `NEW`
- `IN_PROGRESS`
- `PENDING_PART`
- `PENDING_CUSTOMER`
- `PENDING_PAYMENT`
- `APPLIANCE_INSTALLED`
- `CLOSED`

### `WorkOrderPriority`
- `1 = LOW` → Baja
- `2 = MID` → Media
- `3 = HIGH` → Alta
- `4 = URGENT` → Urgente

### `ChargeType`
- `VISIT`
- `REPAIR`
- `INSTALLATION`
- `NOT_SPECIFIED`
- `OTHER`

### `PaymentMethod`
- `CASH`
- `CARD`
- `BIZUM`
- `TRANSFER`
- `NOT_SPECIFIED`
- `OTHER`

## Relaciones clave del dominio

```text
Client 1 ─── N Address 1 ─── N Appliance
                    │
                    └─────── N:M WorkOrder

User   1 ─── N WorkOrder
Client 1 ─── N WorkOrder
WorkOrder 1 ─── N WorkOrderCharge
```

## Auditoría automática
Callbacks JPA aplicados en el modelo:
- `User`: establece `createdAt` y `lastUpdatedAt`.
- `Client`: establece `createdAt`.
- `WorkOrder`: actualiza `createdAt` y `lastUpdatedAt`.
- `WorkOrderCharge`: establece `createdAt`.

## Consideraciones para frontend o integraciones
- Los DTOs de detalle devuelven estructuras enriquecidas para evitar múltiples llamadas encadenadas.
- Algunos listados usan DTOs reducidos, distintos de los DTOs de detalle.
- El recurso `Appliance` puede seguir existiendo aunque esté inactivo, por lo que conviene distinguir entre borrado lógico y existencia histórica.