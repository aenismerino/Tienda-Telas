package com.tienda.inventario.service;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.model.Producto;
import com.tienda.inventario.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarProducto_Exito() {
        // Given
        ProductoDTO dto = new ProductoDTO();
        dto.setPrecio(1000);
        dto.setStock(10);
        dto.setNombre("Seda");

        Producto producto = new Producto();
        producto.setId(1);
        producto.setPrecio(1000);
        producto.setStock(10);
        producto.setNombre("Seda");

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        ProductoDTO result = productoService.guardar(dto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Seda", result.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testGuardarProducto_PrecioNegativo() {
        // Given
        ProductoDTO dto = new ProductoDTO();
        dto.setPrecio(-1000);
        dto.setStock(10);

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.guardar(dto);
        });

        assertEquals("El precio o el stock no pueden ser menores a 0", exception.getMessage());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testBuscarPorId() {
        // Given
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Algodon");

        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        // When
        ProductoDTO result = productoService.buscarPorId(1);

        // Then
        assertNotNull(result);
        assertEquals("Algodon", result.getNombre());
    }
}
