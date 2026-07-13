# Guía de Uso: Endpoints CRUD (Tienda de Telas)

Este documento contiene ejemplos prácticos de cómo interactuar con los microservicios. Recuerda que, excepto para el Login/Registro, **siempre debes enviar el token JWT** en los Headers de tu petición.

## 🔑 1. Autenticación (`auth-service`)

Para obtener el token que te dará acceso a los demás CRUDs, primero debes loguearte.

### Login
- **Ruta:** `POST /auth/login` o `POST /api/auth/login` (según tu gateway)
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "email": "cliente@correo.com",
  "password": "mi_password_123"
}
```
**Respuesta exitosa (200 OK):** Recibirás un JSON con el campo `token`. Copia ese chorro de texto.

---

## 🛠️ Cómo usar el Token en los CRUDs
En todas las peticiones a continuación, debes ir a la pestaña "Headers" (en Postman o Insomnia) o "Auth -> Bearer Token" y colocar:
`Authorization: Bearer <TU_TOKEN_AQUI>`

---

## 📦 2. Inventario (`inventario-service`)

### Crear un Producto (Tela)
- **Ruta:** `POST /productos/crear`
- **Body:**
```json
{
  "nombre": "Seda Premium Azul",
  "descripcion": "Tela suave para vestidos de noche",
  "precio": 15000,
  "stock": 50
}
```
*(Recuerda: Si envías un precio de `-10`, el servicio te devolverá un elegante Error 400 gracias a tus validaciones `@Min`).*

### Obtener con HATEOAS (Nivel 3 REST)
- **Ruta:** `GET /productos/v2`
- **Respuesta:** En lugar de solo datos, verás una sección `_links` que indica cómo acceder al producto específico.

---

## 🛍️ 3. Pedidos (`pedido-service`)

### Crear un Pedido
- **Ruta:** `POST /pedidos/crear`
- **Body:**
```json
{
  "productoId": 1,
  "cantidad": 2,
  "precioUnitario": 15000
}
```
*(El microservicio calculará automáticamente el `totalPedido` como 30000).*

---

## 💳 4. Pagos (`pago-service`)

### Procesar el Pago de un Pedido
- **Ruta:** `POST /payments`
- **Body:**
```json
{
  "orderId": 1,
  "monto": 30000
}
```
*(Al ejecutar esto, el `pago-service` se comunicará en secreto vía OpenFeign con el `pedido-service` para cambiar el estado del pedido a "PAGADO").*

---

## 🚚 5. Envíos (`envio-service`)

### Registrar Envío Inicial
- **Ruta:** `POST /shipments`
- **Body:**
```json
{
  "orderId": 1,
  "direccionDestino": "Av. Siempreviva 742, Springfield"
}
```

### Actualizar el Tracking (Historial)
- **Ruta:** `PUT /shipments/1/tracking`
- **Body:**
```json
{
  "nuevaDescripcion": "EN REPARTO"
}
```
*(Al consultar el envío por GET, verás un arreglo `historial` que contiene la fecha y hora exacta de cada cambio de estado).*

---

## 🎟️ 6. Soporte (`support-service`)

### Crear un Ticket de Ayuda
- **Ruta:** `POST /api/tickets`
- **Body:**
```json
{
  "asunto": "Retraso en mi envío",
  "descripcion": "Compré una seda hace 3 días y sigue en Preparación."
}
```
*(El estado quedará automáticamente en "ABIERTO").*

---

> [!WARNING]
> **Probando los Errores (La magia de tu GlobalExceptionHandler)**
> Intenta hacer un `GET /productos/99999` (un ID que no existe). 
> Verás que ya no te aparece el error genérico feo de Tomcat, sino tu JSON estandarizado:
> ```json
> {
>   "timestamp": "2026-07-08T15:00:00Z",
>   "status": 404,
>   "error": "Not Found",
>   "message": "Producto no encontrado con ID: 99999",
>   "path": "/productos/99999"
> }
> ```
