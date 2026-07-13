package com.tienda.carrito.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tienda.carrito.dto.CarritoItemDTO;
import com.tienda.carrito.dto.CarritoResponseDTO;
import com.tienda.carrito.model.CarritoItem;
import com.tienda.carrito.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @InjectMocks
    private CarritoService carritoService;

    @Mock
    private CarritoRepository carritoRepository;

    private CarritoItem item1;
    private CarritoItem item2;

    @BeforeEach
    void setUp() {
        item1 = new CarritoItem();
        item1.setId(1);
        item1.setUsuarioId("user123");
        item1.setProductoId(10);
        item1.setCantidad(2);
        item1.setPrecioUnitario(5000);
        item1.setSeleccionado(true);

        item2 = new CarritoItem();
        item2.setId(2);
        item2.setUsuarioId("user123");
        item2.setProductoId(20);
        item2.setCantidad(1);
        item2.setPrecioUnitario(3000);
        item2.setSeleccionado(false); // No sumará al totalPagar
    }

    @Test
    void testObtenerCarritoPorUsuario() {
        when(carritoRepository.findByUsuarioId("user123")).thenReturn(List.of(item1, item2));
        
        CarritoResponseDTO respuesta = carritoService.obtenerCarritoPorUsuario("user123");
        
        assertNotNull(respuesta);
        assertEquals(2, respuesta.getItems().size());
        
        // Math check:
        // Item 1: 2 * 5000 = 10000 (seleccionado = true)
        // Item 2: 1 * 3000 = 3000 (seleccionado = false)
        // Total carrito = 13000, Total a pagar = 10000
        assertEquals(13000, respuesta.getTotalCarrito());
        assertEquals(10000, respuesta.getTotalPagar());
    }

    @Test
    void testAgregarProductoNuevo() {
        when(carritoRepository.findByUsuarioId("user123")).thenReturn(List.of());
        when(carritoRepository.save(any(CarritoItem.class))).thenReturn(item1);

        CarritoItemDTO dto = new CarritoItemDTO();
        dto.setUsuarioId("user123");
        dto.setProductoId(10);
        dto.setCantidad(2);
        dto.setPrecioUnitario(5000);

        CarritoItemDTO resultado = carritoService.agregarProducto(dto);
        assertNotNull(resultado);
        assertEquals(10, resultado.getProductoId());
        verify(carritoRepository).save(any(CarritoItem.class));
    }

    @Test
    void testCambiarSeleccion() {
        when(carritoRepository.findById(1)).thenReturn(Optional.of(item1));
        
        CarritoItem itemActualizado = new CarritoItem();
        itemActualizado.setSeleccionado(false);
        when(carritoRepository.save(any(CarritoItem.class))).thenReturn(itemActualizado);

        CarritoItemDTO resultado = carritoService.cambiarSeleccion(1, false);
        assertFalse(resultado.getSeleccionado());
    }

    @Test
    void testActualizarCantidad() {
        when(carritoRepository.findById(1)).thenReturn(Optional.of(item1));
        
        CarritoItem itemActualizado = new CarritoItem();
        itemActualizado.setCantidad(5);
        when(carritoRepository.save(any(CarritoItem.class))).thenReturn(itemActualizado);

        CarritoItemDTO resultado = carritoService.actualizarCantidad(1, 5);
        assertEquals(5, resultado.getCantidad());
    }

    @Test
    void testEliminarItemDelCarrito() {
        when(carritoRepository.existsById(1)).thenReturn(true);
        doNothing().when(carritoRepository).deleteById(1);

        assertDoesNotThrow(() -> carritoService.eliminarItemDelCarrito(1));
        verify(carritoRepository, times(1)).deleteById(1);
    }
}
