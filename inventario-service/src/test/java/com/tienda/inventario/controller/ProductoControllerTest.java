package com.tienda.inventario.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.inventario.dto.ProductoDTO;
import com.tienda.inventario.model.Producto;
import com.tienda.inventario.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Lana de Alpaca");
        producto.setTipo("Lana");
        producto.setDescripcion("Lana super abrigadora");
        producto.setPrecio(25000);
        producto.setStock(100);
        producto.setMetros(5.0);

        productoDTO = ProductoDTO.fromModel(producto);

        mockMvc = MockMvcBuilders.standaloneSetup(productoController).build();
    }

    @Test
    public void testListarProductos() throws Exception {
        when(productoService.listarTodo()).thenReturn(List.of(producto));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Lana de Alpaca"))
                .andExpect(jsonPath("$[0].precio").value(25000));
    }

    @Test
    public void testObtenerProductoPorId() throws Exception {
        when(productoService.buscarPorId(1)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Lana de Alpaca"));
    }

    @Test
    public void testObtenerProductoPorIdNoEncontrado() throws Exception {
        when(productoService.buscarPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGuardarProducto() throws Exception {
        when(productoService.guardar(any(ProductoDTO.class))).thenReturn(producto);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Lana de Alpaca"));
    }

    @Test
    public void testActualizar() throws Exception {
        when(productoService.actualizar(eq(1), any(ProductoDTO.class))).thenReturn(producto);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Lana de Alpaca"));
    }

    @Test
    public void testEliminar() throws Exception {
        doNothing().when(productoService).eliminar(1);

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).eliminar(1);
    }
}
