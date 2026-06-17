# Explicación del Código - Proyecto Semestral (Evaluación 2)

Este documento contiene la explicación detallada de los códigos y configuraciones principales implementadas en los microservicios. Puedes utilizar esta información para armar tu informe y prepararte para la defensa del proyecto.

---

## 1. Arquitectura de Microservicios y Base de Datos

El proyecto consta de **5 microservicios**, cada uno con su propia base de datos, lo que cumple el patrón de base de datos por servicio (*Database per service*):
1. **auth-service:** Gestión de usuarios, roles y autenticación (`db_auth`).
2. **inventario:** Gestión de productos (`bd_inventario`).
3. **pedido:** Gestión de pedidos y carritos de compras (`bd_pedido`).
4. **pago-service:** Procesamiento de pagos (`db_pago`).
5. **envio-service:** Despacho y seguimiento (`db_envio`).

### Relación de Tablas (Microservicio Pedido)
En el microservicio `pedido`, tenemos una base de datos con **tablas relacionadas**. 
* **`Pedido.java`**: Es la entidad principal. Utilizamos `@OneToMany` para indicar que un solo pedido puede contener múltiples ítems. `cascade = CascadeType.ALL` asegura que si se guarda o borra un pedido, también se guardarán o borrarán sus ítems.
* **`CarritoItem.java`**: Es el ítem específico. Usamos `@ManyToOne` y `@JoinColumn(name = "pedido_id")` para conectarlo físicamente mediante una clave foránea (Foreign Key) a la tabla `pedido`.

```java
// En Pedido.java
@OneToMany(mappedBy = "pedido", cascade = jakarta.persistence.CascadeType.ALL)
private List<CarritoItem> items;

// En CarritoItem.java
@ManyToOne
@JoinColumn(name = "pedido_id")
@JsonIgnore // Evita bucles infinitos al convertir a JSON
private Pedido pedido;
```

---

## 2. Migraciones de Base de Datos (Liquibase y Flyway)

Para inicializar las bases de datos y poblarlas con los **10 registros exigidos**, utilizamos herramientas de migración.

### Liquibase (Usado en Pedido, Auth e Inventario)
Liquibase utiliza archivos XML (o YAML) para controlar versiones de la base de datos (Changesets). 
* Utilizamos `<createTable>` para generar las tablas.
* Utilizamos `<addForeignKeyConstraint>` para vincular `carrito_items` con `pedido`.
* Utilizamos `<insert>` para inyectar 10 registros por defecto al levantar el proyecto.

### Flyway (Usado en Envio-Service)
Flyway es la alternativa a Liquibase (Bonus Track 1). En lugar de XML, utiliza scripts de SQL puro. Creamos el archivo `V1__init.sql`:
```sql
CREATE TABLE envios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    -- ...
);
INSERT INTO envios (...) VALUES (...);
```
*El `V1__` indica la Versión 1. Al iniciar la app, Spring Boot detecta el archivo y lo ejecuta en la base de datos automáticamente.*

---

## 3. Comunicación entre Microservicios (FeignClients)

Para que los microservicios hablen entre sí (Ej: que Pedido le pregunte a Inventario si hay stock), usamos **OpenFeign**.
1. Se instala la dependencia `spring-cloud-starter-openfeign`.
2. Se activa agregando `@EnableFeignClients` en la clase principal (Ej: `PedidoApplication.java`).
3. Se crea una "Interfaz" que simula un Controlador pero en realidad **consume** la API de otro microservicio.

**Ejemplo: Pedido comunicándose con Inventario (Punto 5 y 6)**
```java
@FeignClient(name = "inventario-service", url = "http://localhost:8083/productos")
public interface InventarioClient {

    // Al llamar este método, internamente se hace una petición HTTP GET a localhost:8083
    @GetMapping("/{id}")
    Object obtenerProductoPorId(@PathVariable("id") Integer id);
}
```
*Esto cumple el requisito de tener microservicios comunicándose con 1 o 2 microservicios distintos.*

---

## 4. Endpoints Especiales (Totales y Búsquedas)

Se exigía crear métodos de búsqueda especiales y totales. Esto se resolvió utilizando **Java Streams** y **Filtros** en la capa de Servicio (`@Service`).

**Ejemplo 1: Suma total de ventas en PedidoService**
```java
public Integer obtenerTotalVentas() {
    return pedidoRepository.findAll().stream()
            .mapToInt(Pedido::getTotalPedido) // Extraemos solo el total de cada pedido
            .sum(); // Sumamos todos los totales
}
```

**Ejemplo 2: Búsqueda por rango de fechas en PedidoService**
```java
public List<PedidoDTO> buscarPorFecha(LocalDateTime inicio, LocalDateTime fin) {
    return pedidoRepository.findAll().stream()
            // Filtramos fechas que no sean "antes del inicio" ni "después del fin"
            .filter(p -> p.getFechaPedido() != null && !p.getFechaPedido().isBefore(inicio) && !p.getFechaPedido().isAfter(fin))
            .map(this::convertirADto)
            .collect(Collectors.toList());
}
```

---

## 5. Logging (Rastreo de Errores)

Para cumplir el requisito de usar Logs en 2 microservicios, usamos la librería **SLF4J** que viene integrada con **Lombok**.
Basta con poner la anotación `@Slf4j` encima del Controlador.

```java
@Slf4j
@RestController
public class PedidoController {
    
    @GetMapping
    public List<PedidoDTO> listar() {
        // Se imprimirá en la consola de Spring Boot
        log.info("Listando todos los pedidos"); 
        return pedidoService.listarTodo();
    }
}
```
*Se usa `log.info()` para información general, y `log.error()` dentro de bloques `catch` para registrar excepciones.*

---

## 6. Seguridad con JWT (Bonus Track 2)

En el `auth-service`, implementamos JSON Web Tokens (JWT) para la autenticación. 
* **`JwtUtil.java`**: Es la clase que fabrica el token. Toma el RUT del usuario, le pone una fecha de caducidad (10 horas en el futuro) y lo firma criptográficamente usando una contraseña secreta (`SECRET_KEY`).
* **`AuthController.java` (`/login`)**: Recibe RUT y Contraseña. Si coinciden con la base de datos, llama a `JwtUtil` para generar el código alfanumérico (Token) y se lo devuelve al cliente para que lo use en futuras peticiones protegidas.

```java
// Fragmento generador del token en JwtUtil
return Jwts.builder()
        .setSubject(rut) // El "dueño" del token
        .setIssuedAt(new Date()) // Fecha de creación
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // Firma
        .compact();
```

---
*Con esta guía podrás explicar cada concepto técnico utilizado en el proyecto. ¡Mucho éxito en tu presentación!*
