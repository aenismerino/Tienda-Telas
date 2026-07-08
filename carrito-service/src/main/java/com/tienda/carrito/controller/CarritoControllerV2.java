package com.tienda.carrito.controller;

import com.tienda.carrito.dto.CarritoResponseDTO;
import com.tienda.carrito.assemblers.CarritoModelAssembler;
import com.tienda.carrito.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito/v2")
@RequiredArgsConstructor
public class CarritoControllerV2 {

    private final CarritoService carritoService;
    private final CarritoModelAssembler assembler;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<CarritoResponseDTO>> obtenerCarritoV2(@PathVariable String usuarioId) {
        CarritoResponseDTO carrito = carritoService.obtenerCarritoPorUsuario(usuarioId);
        return ResponseEntity.ok(assembler.toModel(carrito));
    }
}
