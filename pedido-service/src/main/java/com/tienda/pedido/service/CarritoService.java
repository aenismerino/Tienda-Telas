package com.tienda.pedido.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.pedido.dto.CarritoItemDTO;
import com.tienda.pedido.dto.CarritoResponseDTO;
import com.tienda.pedido.model.CarritoItem;
import com.tienda.pedido.repository.CarritoRepository;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

 
    public CarritoResponseDTO obtenerCarritoPorUsuario(Integer usuarioId) {
        List<CarritoItem> items = carritoRepository.findByUsuarioId(usuarioId);
        
        int totalCarrito = 0;
        int totalPagar = 0;
        
        for (CarritoItem item : items) {
            int subtotal = item.getCantidad() * item.getPrecioUnitario();
            totalCarrito += subtotal;
            
            if (Boolean.TRUE.equals(item.getSeleccionado())) {
                totalPagar += subtotal;
            }
        }
        
        List<CarritoItemDTO> itemsDto = items.stream()
                .map(this::convertirADto)
                .toList();
        
        CarritoResponseDTO respuesta = new CarritoResponseDTO();
        respuesta.setItems(itemsDto);
        respuesta.setTotalCarrito(totalCarrito);
        respuesta.setTotalPagar(totalPagar);
        
        return respuesta;
    }

    
    public CarritoItemDTO agregarProducto(CarritoItemDTO dto) {
        List<CarritoItem> carritoActual = carritoRepository.findByUsuarioId(dto.getUsuarioId());
        
        
        Optional<CarritoItem> itemExistente = carritoActual.stream()
                .filter(item -> item.getProductoId().equals(dto.getProductoId()))
                .findFirst();
                
        CarritoItem itemAEditar;
        
        if (itemExistente.isPresent()) {
            itemAEditar = itemExistente.get();
            itemAEditar.setCantidad(itemAEditar.getCantidad() + dto.getCantidad());
        } else {

            itemAEditar = new CarritoItem();
            itemAEditar.setUsuarioId(dto.getUsuarioId());
            itemAEditar.setProductoId(dto.getProductoId());
            itemAEditar.setCantidad(dto.getCantidad());
            itemAEditar.setPrecioUnitario(dto.getPrecioUnitario());
            itemAEditar.setSeleccionado(true); 
        }
        
        CarritoItem guardado = carritoRepository.save(itemAEditar);
        return convertirADto(guardado);
    }

    
    public CarritoItemDTO cambiarSeleccion(Integer itemId, Boolean seleccionado) {
        CarritoItem item = carritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));
        
        item.setSeleccionado(seleccionado);
        CarritoItem actualizado = carritoRepository.save(item);
        return convertirADto(actualizado);
    }

    
    private CarritoItemDTO convertirADto(CarritoItem item) {
        CarritoItemDTO dto = new CarritoItemDTO();
        dto.setId(item.getId());
        dto.setUsuarioId(item.getUsuarioId());
        dto.setProductoId(item.getProductoId());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setSeleccionado(item.getSeleccionado());
        return dto;
    }

    public CarritoItemDTO actualizarCantidad(Integer itemId, Integer nuevaCantidad) {
        CarritoItem item = carritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        item.setCantidad(nuevaCantidad);
        CarritoItem actualizado = carritoRepository.save(item);
        return convertirADto(actualizado);
    }


    public void eliminarItemDelCarrito(Integer itemId) {
        if (!carritoRepository.existsById(itemId)) {
            throw new RuntimeException("El item no existe");
        }
        carritoRepository.deleteById(itemId);
    }
}
