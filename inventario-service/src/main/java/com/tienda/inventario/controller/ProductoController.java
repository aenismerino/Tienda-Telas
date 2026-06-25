package com.tienda.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.service.ProductoService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService; // Inyectamos la lógica

    
    @GetMapping
    public CollectionModel<EntityModel<ProductoDTO>> listar() {
        List<EntityModel<ProductoDTO>> productos = productoService.listarTodo().stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).buscarPorId(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).listar()).withRel("productos")))
                .collect(Collectors.toList());
        return CollectionModel.of(productos, linkTo(methodOn(ProductoController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<ProductoDTO> buscarPorId(@PathVariable Integer id) {
        ProductoDTO producto = productoService.buscarPorId(id);
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listar()).withRel("productos"));
    }

    @PostMapping
    public ProductoDTO crear(@RequestBody ProductoDTO productoDTO) {
        return productoService.guardar(productoDTO);
    }

    @PutMapping("/{id}")
    public ProductoDTO actualizar(@PathVariable Integer id, @RequestBody ProductoDTO productoDTO) {
        return productoService.actualizar(id, productoDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
    }
}