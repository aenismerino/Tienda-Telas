# Arquitectura del Sistema: Tienda de Telas

Este documento describe a alto nivel el rol, la responsabilidad y las tecnologías de cada uno de los microservicios que componen el ecosistema de la Tienda de Telas.

## 🛠️ Stack Tecnológico Global
Todos los microservicios comparten el siguiente estándar de desarrollo:
- **Framework:** Spring Boot (Web, Data JPA).
- **Seguridad:** Spring Security con JSON Web Tokens (JWT).
- **Validación:** JSR 380 (Hibernate Validator / `spring-boot-starter-validation`).
- **Nivel de Madurez REST:** Nivel 3 (HATEOAS).
- **Base de Datos:** MySQL (con migraciones vía Liquibase).
- **Comunicación Interna:** Spring Cloud OpenFeign.

---

## 🏗️ Los Microservicios

### 1. `api-gateway` (El Portero)
- **Rol:** Es el único punto de entrada público para el Frontend.
- **Responsabilidad:** Enruta las peticiones HTTP al microservicio correspondiente. Maneja políticas de CORS y valida la estructura global de los JWT antes de dejarlos pasar a la red interna.
- **Dato Clave:** Está construido sobre Spring WebFlux (reactivo), no usa WebMVC.

### 2. `auth-service` (El Guardia)
- **Rol:** Gestión de identidad.
- **Responsabilidad:** Maneja el registro de nuevos usuarios, la autenticación (Login) y emite los tokens JWT que el resto de los microservicios usarán para validar identidades.

### 3. `inventario-service` (El Almacén)
- **Rol:** Catálogo maestro de telas.
- **Responsabilidad:** CRUD completo de productos. Controla el stock, los precios y las descripciones de las telas.

### 4. `pedido-service` (Las Ventas)
- **Rol:** Corazón del negocio.
- **Responsabilidad:** Gestiona la creación de órdenes de compra, asocia productos con usuarios y calcula totales.

### 5. `pago-service` (El Cajero)
- **Rol:** Procesamiento financiero.
- **Responsabilidad:** Registra y valida los pagos de los pedidos. Se comunica de vuelta con el `pedido-service` (vía Feign) para actualizar el estado del pedido a "PAGADO".

### 6. `envio-service` (La Logística)
- **Rol:** Despacho y Tracking.
- **Responsabilidad:** Recibe la orden de despacho y mantiene un historial (`TrackingHistory`) para que el usuario sepa si su paquete está en "PREPARACIÓN", "EN CAMINO" o "ENTREGADO".

### 7. `resenas-service` (La Comunidad)
- **Rol:** Feedback del usuario.
- **Responsabilidad:** Almacena las calificaciones (1 a 5 estrellas) y comentarios que los usuarios dejan en las telas que han comprado.

### 8. `catalogo-service` (El Agregador)
- **Rol:** Optimizador de lectura.
- **Responsabilidad:** No tiene base de datos propia. Su trabajo es hacer llamadas internas (OpenFeign) al `inventario-service` y al `resenas-service`, uniendo la información para devolverle al Frontend un "Producto con sus estrellas" en una sola petición.

### 9. `carrito-service` (El Temporal)
- **Rol:** Cesta de compras.
- **Responsabilidad:** Almacena temporalmente lo que el usuario quiere comprar antes de formalizar el checkout hacia el `pedido-service`.

### 10. `notificaciones-service` (El Comunicador)
- **Rol:** Historial de alertas.
- **Responsabilidad:** Registra los mensajes que el sistema debe enviarle al usuario (ej: "Tu pedido ha sido enviado", "Bienvenido a la tienda").

### 11. `support-service` (Atención al Cliente)
- **Rol:** Mesa de ayuda.
- **Responsabilidad:** Permite a los usuarios crear "Tickets" para resolver problemas con compras, despachos o dudas generales.

---

> [!TIP]
> **Orquestación con Docker**
> Todos estos servicios están diseñados para levantarse en paralelo mediante un `docker-compose.yml`, que crea una red interna donde los servicios pueden comunicarse entre sí por nombre de contenedor (ej: `http://pago-service:8080`) sin salir a internet.
