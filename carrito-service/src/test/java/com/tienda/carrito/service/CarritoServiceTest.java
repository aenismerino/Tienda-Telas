package com.tienda.carrito.service;

import com.tienda.carrito.dto.CarritoResponseDTO;
import com.tienda.carrito.model.CarritoItem;
import com.tienda.carrito.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoService carritoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerCarrito_CalculoIvaYDescuento() {
        // Given: carrito con items que superan los 50.000 para aplicar descuento
        CarritoItem item1 = new CarritoItem();
        item1.setId(1);
        item1.setCantidad(2);
        item1.setPrecioUnitario(30000); // Subtotal = 60000
        item1.setSeleccionado(true);

        when(carritoRepository.findByUsuarioId("user1")).thenReturn(Arrays.asList(item1));

        // When
        CarritoResponseDTO response = carritoService.obtenerCarritoPorUsuario("user1");

        // Then
        // Subtotal = 60000
        // IVA (19%) = 11400
        // Descuento (10%) = 6000 (porque > 50000)
        // Total = 60000 + 11400 - 6000 = 65400
        
        assertEquals(60000, response.getTotalCarrito());
        assertEquals(11400, response.getIva());
        assertEquals(6000, response.getDescuento());
        assertEquals(65400, response.getTotalPagar());
    }

    @Test
    void testObtenerCarrito_SinDescuento() {
        // Given: carrito menor a 50.000
        CarritoItem item1 = new CarritoItem();
        item1.setId(1);
        item1.setCantidad(1);
        item1.setPrecioUnitario(10000); // Subtotal = 10000
        item1.setSeleccionado(true);

        when(carritoRepository.findByUsuarioId("user2")).thenReturn(Arrays.asList(item1));

        // When
        CarritoResponseDTO response = carritoService.obtenerCarritoPorUsuario("user2");

        // Then
        // Subtotal = 10000
        // IVA (19%) = 1900
        // Descuento = 0 (porque no > 50000)
        // Total = 10000 + 1900 - 0 = 11900
        
        assertEquals(10000, response.getTotalCarrito());
        assertEquals(1900, response.getIva());
        assertEquals(0, response.getDescuento());
        assertEquals(11900, response.getTotalPagar());
    }
}
