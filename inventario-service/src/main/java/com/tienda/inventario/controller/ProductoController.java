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

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService; // Inyectamos la lógica

    
    @GetMapping
    public List<ProductoDTO> listar() {
        return productoService.listarTodo();
    }

    @GetMapping("/{id}")
    public ProductoDTO buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
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