package com.tienda.carrito.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.carrito.dto.CarritoItemDTO;
import com.tienda.carrito.dto.CarritoResponseDTO;
import com.tienda.carrito.service.CarritoService;
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

@ExtendWith(MockitoExtension.class)
public class CarritoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private CarritoController carritoController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private CarritoItemDTO itemDTO;

    @BeforeEach
    void setUp() {
        itemDTO = new CarritoItemDTO();
        itemDTO.setId(1);
        itemDTO.setUsuarioId("user123");
        itemDTO.setProductoId(10);
        itemDTO.setCantidad(2);
        itemDTO.setPrecioUnitario(5000);
        itemDTO.setSeleccionado(true);

        mockMvc = MockMvcBuilders.standaloneSetup(carritoController).build();
    }

    @Test
    public void testObtenerCarrito() throws Exception {
        CarritoResponseDTO response = new CarritoResponseDTO();
        response.setItems(List.of(itemDTO));
        response.setTotalCarrito(10000);
        response.setTotalPagar(10000);

        when(carritoService.obtenerCarritoPorUsuario("user123")).thenReturn(response);

        mockMvc.perform(get("/carrito/usuario/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCarrito").value(10000))
                .andExpect(jsonPath("$.totalPagar").value(10000));
    }

    @Test
    public void testAgregarAlCarrito() throws Exception {
        when(carritoService.agregarProducto(any(CarritoItemDTO.class))).thenReturn(itemDTO);

        mockMvc.perform(post("/carrito/agregar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productoId").value(10));
    }

    @Test
    public void testCambiarSeleccionItem() throws Exception {
        when(carritoService.cambiarSeleccion(1, false)).thenReturn(itemDTO);

        mockMvc.perform(patch("/carrito/seleccionar/1")
                        .param("estado", "false"))
                .andExpect(status().isOk());
    }

    @Test
    public void testActualizarCantidad() throws Exception {
        when(carritoService.actualizarCantidad(1, 5)).thenReturn(itemDTO);

        mockMvc.perform(put("/carrito/actualizar-cantidad/1")
                        .param("cantidad", "5"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEliminarItem() throws Exception {
        doNothing().when(carritoService).eliminarItemDelCarrito(1);

        mockMvc.perform(delete("/carrito/eliminar/1"))
                .andExpect(status().isNoContent());
                
        verify(carritoService, times(1)).eliminarItemDelCarrito(1);
    }
}
