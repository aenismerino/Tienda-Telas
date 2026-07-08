package com.tienda.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.model.Producto;
import com.tienda.inventario.repository.ProductoRepository;
import com.tienda.inventario.exception.ResourceNotFoundException;
import com.tienda.inventario.exception.BadRequestException;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodo() {
        return productoRepository.findAll();
    }

    public Producto guardar(ProductoDTO dto) {
        Producto producto = dto.toModel();
        return productoRepository.save(producto);
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public void eliminar(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    public Producto actualizar(Integer id, ProductoDTO dto) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        existente.setNombre(dto.getNombre());
        existente.setTipo(dto.getTipo());
        existente.setDescripcion(dto.getDescripcion());
        existente.setPrecio(dto.getPrecio());
        existente.setStock(dto.getStock());
        existente.setMetros(dto.getMetros());

        return productoRepository.save(existente);
    }
}