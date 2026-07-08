package com.tienda.inventario.controller;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.assemblers.ProductoModelAssembler;
import com.tienda.inventario.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/productos/v2")
@RequiredArgsConstructor
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> listarProductosV2() {
        log.info("V2 GET /productos/v2 - Listando productos con HATEOAS");
        List<EntityModel<ProductoDTO>> productos = productoService.listarTodo().stream()
                .map(ProductoDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ProductoDTO>> collectionModel = CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).listarProductosV2()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerProductoV2(@PathVariable Integer id) {
        log.info("V2 GET /productos/v2/{} - Buscando producto con HATEOAS", id);
        return productoService.buscarPorId(id)
                .map(ProductoDTO::fromModel)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
