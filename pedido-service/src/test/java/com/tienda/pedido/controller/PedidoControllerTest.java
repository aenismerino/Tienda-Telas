package com.tienda.pedido.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tienda.pedido.dto.PedidoDTO;
import com.tienda.pedido.model.Pedido;
import com.tienda.pedido.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private ObjectMapper objectMapper;

    private Pedido pedido;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        pedido = new Pedido();
        pedido.setId(1);
        pedido.setProductoId(10);
        pedido.setCantidad(2);
        pedido.setPrecioUnitario(5000);
        pedido.setTotalPedido(10000);
        pedido.setFechaPedido(LocalDateTime.now());

        pedidoDTO = PedidoDTO.fromModel(pedido);

        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController).build();
    }

    @Test
    public void testListarPedidos() throws Exception {
        when(pedidoService.listarTodo()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productoId").value(10))
                .andExpect(jsonPath("$[0].totalPedido").value(10000));
    }

    @Test
    public void testObtenerPedidoPorId() throws Exception {
        when(pedidoService.buscarPorId(1)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productoId").value(10));
    }

    @Test
    public void testCrearPedido() throws Exception {
        when(pedidoService.guardar(any(PedidoDTO.class))).thenReturn(pedido);

        mockMvc.perform(post("/pedidos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPedido").value(10000));
    }

    @Test
    public void testActualizarPedido() throws Exception {
        when(pedidoService.actualizar(eq(1), any(PedidoDTO.class))).thenReturn(pedido);

        mockMvc.perform(put("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPedido").value(10000));
    }

    @Test
    public void testEliminarPedido() throws Exception {
        doNothing().when(pedidoService).eliminar(1);

        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());

        verify(pedidoService, times(1)).eliminar(1);
    }

    @Test
    public void testActualizarEstadoPedido() throws Exception {
        mockMvc.perform(put("/pedidos/1/estado")
                        .param("estado", "ENVIADO"))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerTotales() throws Exception {
        when(pedidoService.obtenerTotalVentas()).thenReturn(10000);

        mockMvc.perform(get("/pedidos/totales"))
                .andExpect(status().isOk())
                .andExpect(content().string("10000"));
    }
}
