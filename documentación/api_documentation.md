# Guía de APIs y CRUDs para Postman

Agregar ModHeader al navegador para mostrar la interfaz del Swagger
Authorization   Bearer (Token entregado por el Postman al momento de hacer el login)


Esta guía detalla cómo probar todos los endpoints (CRUDs) de tus microservicios utilizando **Postman** a través del **API Gateway** (que corre en el puerto `8080`).

---

## 1. Autenticación y Uso del Token (JWT)

Para acceder a las rutas protegidas, primero debes iniciar sesión para obtener un **Token JWT** y luego incluirlo en cada petición.

### Paso 1: Obtener el Token (Login)
- **Método:** `POST`
- **URL:** `http://localhost:8080/api/auth/login`
- **Body (JSON):**
  ```json
  {
    "rut": "10101010-0",
    "password": "admin"
  }
  ```
- **Respuesta Esperada:** Recibirás un texto largo que es tu JWT (ej: `eyJhbGciOiJIUzUxMi...`). Cópialo.

### Paso 2: Usar el Token en Postman
Para el resto de las peticiones, debes enviar este token:
1. Ve a la pestaña **Authorization** en Postman.
2. Selecciona el tipo **Bearer Token**.
3. Pega el token que copiaste en el campo **Token**.

---

## 2. Usuarios y Roles Disponibles

En el archivo `auth-service`, tienes los siguientes usuarios predeterminados para probar los roles:
- **Administrador:** RUT `10101010-0` | Clave: `admin` | Rol: `ROLE_ADMIN`
- **Usuario Normal:** RUT `11111111-1` | Clave: `123456` | Rol: `ROLE_USER`

> [!IMPORTANT]
> Algunas rutas (como listar todos los usuarios) están bloqueadas para usuarios normales/compradores y solo pueden ser usadas si te logueaste como Administrador (`ROLE_ADMIN`) o Vendedor (`ROLE_VENDEDOR`).

---

## 3. Endpoints por Microservicio

A continuación se listan todos los CRUDs. Recuerda usar siempre el puerto `8080` (API Gateway).

### 🔐 Usuarios (`auth-service`)

| Acción | Método | URL | Body (Ejemplo) / Notas |
|--------|--------|-----|------------------------|
| **Login** | `POST` | `/api/auth/login` | `{"rut":"...","password":"..."}` |
| **Registrar** | `POST` | `/api/auth/register` | `{"rut":"1-9","nombres":"Juan","apellidos":"Perez",...}` |
| **Listar Todos** | `GET` | `/api/auth/users` | *(Requiere ROLE_ADMIN o ROLE_VENDEDOR)* |
| **Buscar por RUT** | `GET` | `/api/auth/users/{rut}` | *(Requiere ROLE_ADMIN o ROLE_VENDEDOR)* |
| **Actualizar** | `PUT` | `/api/auth/users/{rut}` | Enviar todos los datos actualizados en JSON |
| **Eliminar** | `DELETE`| `/api/auth/users/{rut}` | Elimina al usuario |

---

### 📦 Inventario y Productos (`inventario-service`)

| Acción | Método | URL | Body (Ejemplo) / Notas |
|--------|--------|-----|------------------------|
| **Listar Todos** | `GET` | `/api/productos` | Retorna todo el catálogo de telas |
| **Buscar por ID**| `GET` | `/api/productos/{id}` | Busca una tela específica |
| **Crear** | `POST` | `/api/productos` | `{"nombre":"Seda","precio":15000,"stock":10,...}` |
| **Actualizar** | `PUT` | `/api/productos/{id}` | Enviar datos actualizados en JSON |
| **Eliminar** | `DELETE`| `/api/productos/{id}` | Elimina el producto del inventario |

---

### 🛒 Carrito de Compras (`carrito-service`)

| Acción | Método | URL | Body (Ejemplo) / Notas |
|--------|--------|-----|------------------------|
| **Ver Carrito** | `GET` | `/api/carrito/usuario/{rut}` | Ej: `/api/carrito/usuario/11111111-1` |
| **Agregar Item** | `POST` | `/api/carrito/agregar` | `{"usuarioId":"11111111-1", "productoId":1, "cantidad":2, "precioUnitario":15000}` |
| **Actualizar Cantidad** | `PUT` | `/api/carrito/actualizar-cantidad/{itemId}?cantidad=X` | Cambia la cantidad de un item |
| **Seleccionar Item** | `PATCH`| `/api/carrito/seleccionar/{itemId}?estado=true` | Marca/desmarca para comprar |
| **Eliminar Item** | `DELETE`| `/api/carrito/eliminar/{itemId}` | Quita el item del carrito |

---

### 📋 Pedidos (`pedido-service`)

| Acción | Método | URL | Body (Ejemplo) / Notas |
|--------|--------|-----|------------------------|
| **Listar Todos** | `GET` | `/api/pedidos` | Retorna el historial de pedidos |
| **Buscar por ID**| `GET` | `/api/pedidos/{id}` | Busca un pedido específico |
| **Crear** | `POST` | `/api/pedidos/crear` | `{"productoId":1, "cantidad":2, "precioUnitario":1000}` |
| **Actualizar** | `PUT` | `/api/pedidos/{id}` | Enviar datos actualizados en JSON |
| **Cambiar Estado**| `PUT` | `/api/pedidos/{id}/estado?estado=EN_PREPARACION`| Actualiza el estado del pedido |
| **Eliminar** | `DELETE`| `/api/pedidos/{id}` | Elimina el pedido |

---

### 💳 Pagos (`pago-service`)

| Acción | Método | URL | Body (Ejemplo) / Notas |
|--------|--------|-----|------------------------|
| **Listar Todos** | `GET` | `/api/payments` | Retorna el historial de pagos |
| **Buscar por ID**| `GET` | `/api/payments/{id}` | Busca un pago específico |
| **Registrar Pago** | `POST` | `/api/payments` | `{"orderId":1, "monto":20000, "metodo":"WEBPAY", "estado":"APROBADO"}` |
| **Actualizar** | `PUT` | `/api/payments/{id}` | Actualizar datos del pago |
| **Eliminar** | `DELETE`| `/api/payments/{id}` | Elimina el pago |

---

### 🚚 Envíos (`envio-service`)

| Acción | Método | URL | Body (Ejemplo) / Notas |
|--------|--------|-----|------------------------|
| **Listar Todos** | `GET` | `/api/shipments` | Retorna historial de envíos |
| **Buscar por ID**| `GET` | `/api/shipments/{id}` | Busca envío por ID |
| **Buscar por RUT** | `GET` | `/api/shipments/buscar-rut/{rut}` | Busca envíos de un usuario |
| **Crear Envío** | `POST` | `/api/shipments` | `{"orderId":1, "direccion":"Calle 123", "rut":"11111111-1"}` |
| **Añadir Tracking** | `PUT` | `/api/shipments/{id}/tracking` | `{"nuevaDescripcion": "En camino", "estado": "EN_TRANSITO"}` |
| **Eliminar** | `DELETE`| `/api/shipments/{id}` | Elimina el envío |

---

> [!TIP]
> Recuerda que al probar en Postman, el **API Gateway (`:8080`)** es el encargado de recibir tu petición y enrutarla automáticamente al microservicio correspondiente. Por eso siempre usas el puerto 8080 y la ruta `/api/...` al principio.
