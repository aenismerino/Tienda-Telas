package com.envio_service.envio_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.envio_service.envio_service.DTO.ShipmentDTO;
import com.envio_service.envio_service.DTO.TrackingDTO;
import com.envio_service.envio_service.model.Shipment;
import com.envio_service.envio_service.service.ShipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ShipmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShipmentService shipmentService;

    @InjectMocks
    private ShipmentController shipmentController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Shipment shipment;
    private ShipmentDTO shipmentDTO;

    @BeforeEach
    void setUp() {
        shipment = new Shipment();
        shipment.setId(1L);
        shipment.setOrderId(100L);
        shipment.setDireccionDestino("Av. Siempreviva 742");
        shipment.setEstadoActual("PREPARANDO PAQUETE");
        shipment.setHistorial(new ArrayList<>());

        shipmentDTO = ShipmentDTO.fromModel(shipment);

        mockMvc = MockMvcBuilders.standaloneSetup(shipmentController).build();
    }

    @Test
    public void testListarEnvios() throws Exception {
        when(shipmentService.obtenerTodos()).thenReturn(List.of(shipment));

        mockMvc.perform(get("/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].direccionDestino").value("Av. Siempreviva 742"));
    }

    @Test
    public void testCrearEnvio() throws Exception {
        when(shipmentService.crearEnvio(any(ShipmentDTO.class))).thenReturn(shipment);

        mockMvc.perform(post("/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shipmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estadoActual").value("PREPARANDO PAQUETE"));
    }

    @Test
    public void testActualizarTracking() throws Exception {
        when(shipmentService.agregarHistorial(eq(1L), any(TrackingDTO.class))).thenReturn(shipment);

        TrackingDTO trackingDTO = new TrackingDTO();
        trackingDTO.setNuevaDescripcion("EN RUTA");

        mockMvc.perform(put("/shipments/1/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testEliminarEnvio() throws Exception {
        doNothing().when(shipmentService).eliminar(1L);

        mockMvc.perform(delete("/shipments/1"))
                .andExpect(status().isNoContent());

        verify(shipmentService, times(1)).eliminar(1L);
    }
}
