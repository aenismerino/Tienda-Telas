package com.tienda.pedido.service;

import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.model.Pedido;
import com.tienda.pedido.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarPedido_Exito() {
        // Given
        PedidoDTO dto = new PedidoDTO();
        dto.setProductoId(1);
        dto.setCantidad(2);
        dto.setPrecioUnitario(1000);

        Pedido pedidoGuardado = new Pedido();
        pedidoGuardado.setId(1);
        pedidoGuardado.setProductoId(1);
        pedidoGuardado.setCantidad(2);
        pedidoGuardado.setPrecioUnitario(1000);
        pedidoGuardado.setTotalPedido(2000);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardado);

        // When
        PedidoDTO result = pedidoService.guardar(dto);

        // Then
        assertNotNull(result);
        assertEquals(2000, result.getTotalPedido());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testGuardarPedido_CantidadCero() {
        // Given
        PedidoDTO dto = new PedidoDTO();
        dto.setProductoId(1);
        dto.setCantidad(0);
        dto.setPrecioUnitario(1000);

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.guardar(dto);
        });

        assertEquals("La cantidad debe ser mayor a 0 y no puede estar vacía", exception.getMessage());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testObtenerTotalVentas() {
        // Given
        Pedido p1 = new Pedido();
        p1.setTotalPedido(1000);
        Pedido p2 = new Pedido();
        p2.setTotalPedido(2500);

        when(pedidoRepository.findAll()).thenReturn(java.util.Arrays.asList(p1, p2));

        // When
        Integer total = pedidoService.obtenerTotalVentas();

        // Then
        assertEquals(3500, total);
    }
}
