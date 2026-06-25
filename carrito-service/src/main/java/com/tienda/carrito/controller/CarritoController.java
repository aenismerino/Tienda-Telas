package com.tienda.carrito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.carrito.dto.CarritoItemDTO;
import com.tienda.carrito.dto.CarritoResponseDTO;
import com.tienda.carrito.service.CarritoService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/carrito")
public class CarritoController {
    @Autowired
    private CarritoService carritoService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<CarritoResponseDTO>> obtenerCarrito(@PathVariable String usuarioId) {
        CarritoResponseDTO carrito = carritoService.obtenerCarritoPorUsuario(usuarioId);
        EntityModel<CarritoResponseDTO> resource = EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).obtenerCarrito(usuarioId)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @PostMapping("/agregar")
    public ResponseEntity<CarritoItemDTO> agregarAlCarrito(@RequestBody CarritoItemDTO dto) {
        return ResponseEntity.ok(carritoService.agregarProducto(dto));
    }

    @PatchMapping("/seleccionar/{itemId}")
    public ResponseEntity<CarritoItemDTO> cambiarSeleccionItem(
            @PathVariable Integer itemId,
            @RequestParam Boolean estado) {
        return ResponseEntity.ok(carritoService.cambiarSeleccion(itemId, estado));
    }

    @PutMapping("/actualizar-cantidad/{itemId}")
    public ResponseEntity<CarritoItemDTO> actualizarCantidad(
            @PathVariable Integer itemId,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(itemId, cantidad));
    }

    @DeleteMapping("/eliminar/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Integer itemId) {
        carritoService.eliminarItemDelCarrito(itemId);
        return ResponseEntity.noContent().build();
    }
}
