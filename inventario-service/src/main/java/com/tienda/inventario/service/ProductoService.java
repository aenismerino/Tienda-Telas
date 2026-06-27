package com.tienda.inventario.service;

import java.util.List;
import java.util.stream.Collectors; // Necesario para transformar la lista

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.model.Producto; // Importante
import com.tienda.inventario.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;


    public List<ProductoDTO> listarTodo(){
        return productoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }  

   
    public ProductoDTO guardar(ProductoDTO dto){
       if (dto.getPrecio() < 0 || dto.getStock() < 0){
            throw new RuntimeException("El precio o el stock no pueden ser menores a 0");
       }
       
       Producto producto = convertirAEntidad(dto);
       return convertirADto(productoRepository.save(producto));
    }
    

    public ProductoDTO buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .map(this::convertirADto)
                .orElseThrow(() -> new RuntimeException("No encontrado"));
    }

    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }


    public ProductoDTO actualizar(Integer id, ProductoDTO productoDetalles) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(productoDetalles.getNombre());
            producto.setTipo(productoDetalles.getTipo());
            producto.setDescripcion(productoDetalles.getDescripcion());
            producto.setPrecio(productoDetalles.getPrecio());
            producto.setStock(productoDetalles.getStock());
            producto.setMetros(productoDetalles.getMetros());
            
            return convertirADto(productoRepository.save(producto)); 
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }


    private ProductoDTO convertirADto(Producto p) {
        ProductoDTO d = new ProductoDTO();
        d.setId(p.getId());
        d.setNombre(p.getNombre());
        d.setTipo(p.getTipo());
        d.setDescripcion(p.getDescripcion());
        d.setPrecio(p.getPrecio());
        d.setStock(p.getStock());
        d.setMetros(p.getMetros());
        return d;
    }

    private Producto convertirAEntidad(ProductoDTO d) {
        Producto p = new Producto();
        p.setId(d.getId());
        p.setNombre(d.getNombre());
        p.setTipo(d.getTipo());
        p.setDescripcion(d.getDescripcion());
        p.setPrecio(d.getPrecio());
        p.setStock(d.getStock());
        p.setMetros(d.getMetros());
        return p;
    }
}