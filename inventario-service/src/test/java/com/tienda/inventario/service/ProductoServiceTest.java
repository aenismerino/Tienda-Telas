package com.tienda.inventario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.model.Producto;
import com.tienda.inventario.repository.ProductoRepository;
import com.tienda.inventario.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @InjectMocks
    private ProductoService productoService;

    @Mock
    private ProductoRepository productoRepository;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Seda Premium");
        producto.setTipo("Seda");
        producto.setDescripcion("Seda muy fina");
        producto.setPrecio(15000);
        producto.setStock(50);
        producto.setMetros(10.5);

        productoDTO = ProductoDTO.builder()
                .id(1)
                .nombre("Seda Premium")
                .tipo("Seda")
                .descripcion("Seda muy fina")
                .precio(15000)
                .stock(50)
                .metros(10.5)
                .build();
    }

    @Test
    void testListarTodo() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));
        List<Producto> productos = productoService.listarTodo();
        assertNotNull(productos);
        assertEquals(1, productos.size());
        verify(productoRepository).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        Optional<Producto> resultado = productoService.buscarPorId(1);
        assertTrue(resultado.isPresent());
        assertEquals("Seda Premium", resultado.get().getNombre());
        verify(productoRepository).findById(1);
    }

    @Test
    void testGuardar() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        Producto resultado = productoService.guardar(productoDTO);
        assertNotNull(resultado);
        assertEquals(15000, resultado.getPrecio());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void testActualizar() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        
        Producto actualizado = new Producto();
        actualizado.setId(1);
        actualizado.setNombre("Seda Modificada");
        actualizado.setPrecio(20000);
        when(productoRepository.save(any(Producto.class))).thenReturn(actualizado);

        ProductoDTO dtoActualizar = ProductoDTO.builder()
                .nombre("Seda Modificada")
                .precio(20000)
                .build();

        Producto resultado = productoService.actualizar(1, dtoActualizar);
        assertNotNull(resultado);
        assertEquals("Seda Modificada", resultado.getNombre());
        assertEquals(20000, resultado.getPrecio());
    }

    @Test
    void testActualizarNoEncontrado() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productoService.actualizar(99, productoDTO));
    }

    @Test
    void testEliminar() {
        when(productoRepository.existsById(1)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(1);
        
        assertDoesNotThrow(() -> productoService.eliminar(1));
        verify(productoRepository, times(1)).deleteById(1);
    }

    @Test
    void testEliminarNoEncontrado() {
        when(productoRepository.existsById(99)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> productoService.eliminar(99));
    }
}
