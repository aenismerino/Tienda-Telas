package com.tienda.pedido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.service.PedidoService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.stream.Collectors;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public CollectionModel<EntityModel<PedidoDTO>> listar() {
        log.info("Listando todos los pedidos");
        List<EntityModel<PedidoDTO>> pedidos = pedidoService.listarTodo().stream()
                .map(pedido -> EntityModel.of(pedido,
                        linkTo(methodOn(PedidoController.class).buscarPorId(pedido.getId())).withSelfRel(),
                        linkTo(methodOn(PedidoController.class).listar()).withRel("pedidos")))
                .collect(Collectors.toList());
        return CollectionModel.of(pedidos, linkTo(methodOn(PedidoController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            PedidoDTO pedido = pedidoService.buscarPorId(id);
            EntityModel<PedidoDTO> resource = EntityModel.of(pedido,
                    linkTo(methodOn(PedidoController.class).buscarPorId(id)).withSelfRel(),
                    linkTo(methodOn(PedidoController.class).listar()).withRel("pedidos"));
            return ResponseEntity.ok(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public PedidoDTO crear(@RequestBody PedidoDTO dto) {
        return pedidoService.guardar(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody PedidoDTO dto) {
        try {
            return ResponseEntity.ok(pedidoService.actualizar(id, dto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Pedido no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoPedido(@PathVariable Integer id, @RequestParam("estado") String estado) {
        // En una app real actualizariamos el estado en el servicio
        System.out.println("Actualizando estado de pedido " + id + " a " + estado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/totales")
    public ResponseEntity<Integer> obtenerTotales() {
        return ResponseEntity.ok(pedidoService.obtenerTotalVentas());
    }

    @GetMapping("/buscar-fecha")
    public ResponseEntity<List<PedidoDTO>> buscarPorFecha(
            @RequestParam("inicio") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime inicio,
            @RequestParam("fin") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime fin) {
        return ResponseEntity.ok(pedidoService.buscarPorFecha(inicio, fin));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
