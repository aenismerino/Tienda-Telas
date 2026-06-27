# Documentación Completa de la API y Microservicios (Para Postman)

Esta guía detalla absolutamente todos los endpoints (CRUDs) disponibles en cada uno de los microservicios del proyecto **Tienda de Telas**, pasando a través del **API Gateway** (puerto `8080`).

---

## 1. Autenticación y Uso del Token (JWT)

Para acceder a las rutas protegidas, primero debes iniciar sesión para obtener un **Token JWT** y luego incluirlo en la cabecera `Authorization` de cada petición.

### Obtener el Token (Login)
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/auth/login`
- **Body (JSON):**
  ```json
  {
    "rut": "10101010-0",
    "password": "admin"
  }
  ```
- **Respuesta:** Recibirás tu JWT en texto plano o como string.

### Usuarios Predeterminados
- **Administrador:** RUT `10101010-0` | Clave: `admin` | Rol: `ROLE_ADMIN`
- **Usuario Normal:** RUT `11111111-1` | Clave: `123456` | Rol: `ROLE_USER` / `ROLE_COMPRADOR`

---

## 2. Todos los Endpoints por Microservicio

Todas las peticiones deben dirigirse al puerto **`8080`** (API Gateway). Las rutas base listadas aquí asumen que el API Gateway está configurado para exponerlas bajo el prefijo correspondiente (por ej: `/api/`).

### 🔐 1. Auth Service (`/api/auth`)
Gestión de usuarios y autenticación.

| Acción | Método | URL | Parámetros / Body | Notas |
|--------|--------|-----|-------------------|-------|
| **Login** | `POST` | `/api/auth/login` | `{"rut":"...","password":"..."}` | Retorna Token JWT |
| **Registrar Usuario** | `POST` | `/api/auth/register` | `{"rut":"1-9","nombres":"Juan","apellidos":"Perez","correo":"j@j.cl","telefono":"123","password":"123","roleId":1}` | Registra nuevo usuario |
| **Listar Todos** | `GET` | `/api/auth/users` | - | Requiere `ROLE_ADMIN` o `ROLE_VENDEDOR` |
| **Buscar por RUT** | `GET` | `/api/auth/users/{rut}` | - | Requiere `ROLE_ADMIN` o `ROLE_VENDEDOR` |
| **Actualizar Usuario**| `PUT` | `/api/auth/users/{rut}` | JSON con datos actualizados | Actualiza la información |
| **Eliminar Usuario** | `DELETE`| `/api/auth/users/{rut}` | - | Elimina un usuario por completo |

---

### 📦 2. Inventario Service (`/api/productos`)
Gestión del catálogo de telas y stock.

| Acción | Método | URL | Parámetros / Body | Notas |
|--------|--------|-----|-------------------|-------|
| **Listar Productos** | `GET` | `/api/productos` | - | Retorna todo el catálogo |
| **Buscar por ID** | `GET` | `/api/productos/{id}` | - | Busca una tela específica |
| **Crear Producto** | `POST` | `/api/productos` | `{"nombre":"Seda","descripcion":"Seda pura","precio":15000,"stock":10}` | Crea un nuevo producto |
| **Actualizar** | `PUT` | `/api/productos/{id}` | JSON con datos del producto | Actualiza precio, stock, etc. |
| **Eliminar** | `DELETE`| `/api/productos/{id}` | - | Elimina el producto del inventario |

---

### 🛒 3. Carrito Service (`/api/carrito`)
Gestión de carritos de compras por usuario.

| Acción | Método | URL | Parámetros / Body | Notas |
|--------|--------|-----|-------------------|-------|
| **Ver Carrito** | `GET` | `/api/carrito/usuario/{usuarioId}`| - | Busca el carrito usando el RUT |
| **Agregar Item** | `POST` | `/api/carrito/agregar` | `{"usuarioId":"11111111-1", "productoId":1, "cantidad":2, "precioUnitario":15000}` | Agrega un producto al carrito |
| **Cambiar Selección** | `PATCH`| `/api/carrito/seleccionar/{itemId}?estado=true` | Query Param: `estado=true/false` | Marca el item para compra |
| **Actualizar Cantidad**| `PUT` | `/api/carrito/actualizar-cantidad/{itemId}?cantidad=3`| Query Param: `cantidad=X` | Cambia la cantidad de un item |
| **Eliminar Item** | `DELETE`| `/api/carrito/eliminar/{itemId}`| - | Quita el item del carrito |

---

### 📋 4. Pedido Service (`/api/pedidos`)
Gestión de órdenes de compra.

| Acción | Método | URL | Parámetros / Body | Notas |
|--------|--------|-----|-------------------|-------|
| **Listar Todos** | `GET` | `/api/pedidos` | - | Retorna el historial de pedidos |
| **Buscar por ID** | `GET` | `/api/pedidos/{id}` | - | Busca un pedido específico |
| **Crear Pedido** | `POST` | `/api/pedidos/crear` | `{"productoId":1, "cantidad":2, "precioUnitario":10000,...}` | Crea un pedido nuevo |
| **Actualizar Pedido** | `PUT` | `/api/pedidos/{id}` | JSON con datos del pedido | Actualiza información del pedido |
| **Cambiar Estado** | `PUT` | `/api/pedidos/{id}/estado?estado=PREPARACION` | Query Param: `estado=X` | Cambia estado (ej: ENVIADO) |
| **Eliminar Pedido** | `DELETE`| `/api/pedidos/{id}` | - | Elimina el pedido del historial |
| **Total Ventas** | `GET` | `/api/pedidos/totales` | - | Devuelve el total generado (entero) |
| **Buscar por Fechas** | `GET` | `/api/pedidos/buscar-fecha?inicio=...&fin=...` | Fechas en formato ISO `YYYY-MM-DDTHH:mm:ss` | Filtra pedidos por rango de fecha |

---

### 💳 5. Pago Service (`/api/payments`)
Gestión de transacciones y cobros.

| Acción | Método | URL | Parámetros / Body | Notas |
|--------|--------|-----|-------------------|-------|
| **Listar Pagos** | `GET` | `/api/payments` | - | Retorna historial de pagos |
| **Buscar por ID** | `GET` | `/api/payments/{id}` | - | Busca un pago específico |
| **Registrar Pago** | `POST` | `/api/payments` | `{"orderId":1, "monto":20000, "metodo":"WEBPAY", "estado":"APROBADO"}` | Procesa un nuevo pago |
| **Actualizar Pago** | `PUT` | `/api/payments/{id}` | JSON con datos actualizados | Modifica un pago existente |
| **Eliminar Pago** | `DELETE`| `/api/payments/{id}` | - | Elimina el registro del pago |

---

### 🚚 6. Envío Service (`/api/shipments`)
Gestión de despachos y tracking.

| Acción | Método | URL | Parámetros / Body | Notas |
|--------|--------|-----|-------------------|-------|
| **Listar Envíos** | `GET` | `/api/shipments` | - | Retorna el historial de envíos |
| **Buscar por ID** | `GET` | `/api/shipments/{id}` | - | Busca un envío por ID |
| **Buscar por RUT** | `GET` | `/api/shipments/buscar-rut/{rut}` | - | Busca envíos de un usuario |
| **Crear Envío** | `POST` | `/api/shipments` | `{"orderId":1, "direccion":"Calle 123", "rut":"11111111-1"}` | Crea un envío asociado a un pedido |
| **Añadir Tracking** | `PUT` | `/api/shipments/{id}/tracking` | `{"nuevaDescripcion": "En camino", "estado": "EN_TRANSITO"}` | Actualiza historial de seguimiento |
| **Totales por Estado**| `GET` | `/api/shipments/totales-estado` | - | Retorna conteo agrupado por estados |
| **Eliminar Envío** | `DELETE`| `/api/shipments/{id}` | - | Elimina el registro de envío |

---

### 🔔 7. Notificaciones Service (`/api/notificaciones`)
Historial de alertas y correos (Simulado).

| Acción | Método | URL | Parámetros / Body | Notas |
|--------|--------|-----|-------------------|-------|
| **Listar Todas** | `GET` | `/api/notificaciones` | - | Historial completo de notificaciones |
| **Crear Notificación**| `POST` | `/api/notificaciones` | `{"usuarioId":"1-9", "mensaje":"Tu pedido va en camino"}` | Genera una nueva notificación |

---

### 🛠️ 8. Support Service (`/api/support`)
Gestión de tickets de ayuda / soporte al cliente.

| Acción | Método | URL | Parámetros / Body | Notas |
|--------|--------|-----|-------------------|-------|
| **Listar Tickets** | `GET` | `/api/support` | - | Retorna todos los tickets de soporte |
| **Crear Ticket** | `POST` | `/api/support` | `{"asunto":"Problema", "descripcion":"Falla el pago", "usuarioId":"1-9"}` | Crea un ticket de ayuda |

---

> [!TIP]
> Recuerda que al probar en Postman, el **API Gateway (`:8080`)** es el encargado de recibir tu petición y enrutarla automáticamente al microservicio correspondiente. Por eso siempre usas el puerto 8080 y la ruta `/api/...` al principio (según tu configuración).
