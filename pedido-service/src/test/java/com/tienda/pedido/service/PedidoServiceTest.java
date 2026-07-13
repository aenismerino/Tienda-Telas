package com.tienda.pedido.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.model.Pedido;
import com.tienda.pedido.repository.PedidoRepository;
import com.tienda.pedido.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    private Pedido pedido;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1);
        pedido.setProductoId(10);
        pedido.setCantidad(2);
        pedido.setPrecioUnitario(5000);
        pedido.setTotalPedido(10000);
        pedido.setFechaPedido(LocalDateTime.now());

        pedidoDTO = PedidoDTO.builder()
                .id(1)
                .productoId(10)
                .cantidad(2)
                .precioUnitario(5000)
                .totalPedido(10000)
                .fechaPedido(pedido.getFechaPedido())
                .build();
    }

    @Test
    void testListarTodo() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));
        List<Pedido> pedidos = pedidoService.listarTodo();
        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());
        verify(pedidoRepository).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        Optional<Pedido> resultado = pedidoService.buscarPorId(1);
        assertTrue(resultado.isPresent());
        assertEquals(10, resultado.get().getProductoId());
        verify(pedidoRepository).findById(1);
    }

    @Test
    void testGuardar() {
        // En el servicio se calcula cantidad * precioUnitario = 2 * 5000 = 10000
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        
        Pedido resultado = pedidoService.guardar(pedidoDTO);
        
        assertNotNull(resultado);
        assertEquals(10000, resultado.getTotalPedido());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void testActualizar() {
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        
        Pedido actualizado = new Pedido();
        actualizado.setId(1);
        actualizado.setProductoId(10);
        actualizado.setCantidad(3);
        actualizado.setPrecioUnitario(5000);
        actualizado.setTotalPedido(15000);
        
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(actualizado);

        PedidoDTO dtoActualizar = PedidoDTO.builder()
                .productoId(10)
                .cantidad(3)
                .precioUnitario(5000)
                .build();

        Pedido resultado = pedidoService.actualizar(1, dtoActualizar);
        assertNotNull(resultado);
        assertEquals(3, resultado.getCantidad());
        assertEquals(15000, resultado.getTotalPedido());
    }

    @Test
    void testActualizarNoEncontrado() {
        when(pedidoRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.actualizar(99, pedidoDTO));
    }

    @Test
    void testEliminar() {
        when(pedidoRepository.existsById(1)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(1);
        
        assertDoesNotThrow(() -> pedidoService.eliminar(1));
        verify(pedidoRepository, times(1)).deleteById(1);
    }

    @Test
    void testEliminarNoEncontrado() {
        when(pedidoRepository.existsById(99)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.eliminar(99));
    }

    @Test
    void testObtenerTotalVentas() {
        Pedido pedido2 = new Pedido();
        pedido2.setTotalPedido(5000);
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido, pedido2));

        Integer total = pedidoService.obtenerTotalVentas();
        assertEquals(15000, total);
    }

    @Test
    void testBuscarPorFecha() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.buscarPorFecha(inicio, fin);
        assertEquals(1, resultado.size());
    }
}
