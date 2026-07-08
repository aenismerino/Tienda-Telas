package com.tienda.inventario.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.service.ProductoService;
import com.tienda.inventario.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        log.info("Listando todos los productos (V1)");
        List<ProductoDTO> productos = productoService.listarTodo().stream()
                .map(ProductoDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Integer id) {
        log.info("Buscando producto on ID: {}", id);
        return productoService.buscarPorId(id)
                .map(ProductoDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> guardarProducto(@Valid @RequestBody ProductoDTO dto) {
        log.info("Creando producto: {}", dto.getNombre());
        Producto creado = productoService.guardar(dto);
        return new ResponseEntity<>(ProductoDTO.fromModel(creado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Integer id, @RequestBody @Valid ProductoDTO dto) {
        log.info("Actualizando producto con ID: {}", id);
        Producto actualizado = productoService.actualizar(id, dto);
        return ResponseEntity.ok(ProductoDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("Eliminando producto con ID: {}", id);
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}