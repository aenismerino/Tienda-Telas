package com.envio_service.envio_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.envio_service.envio_service.DTO.ShipmentDTO;
import com.envio_service.envio_service.DTO.TrackingDTO;
import com.envio_service.envio_service.model.Shipment;
import com.envio_service.envio_service.model.TrackingHistory;
import com.envio_service.envio_service.repository.ShipmentRepository;
import com.envio_service.envio_service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @InjectMocks
    private ShipmentService shipmentService;

    @Mock
    private ShipmentRepository shipmentRepository;

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

        shipmentDTO = ShipmentDTO.builder()
                .id(1L)
                .orderId(100L)
                .direccionDestino("Av. Siempreviva 742")
                .estadoActual("PREPARANDO PAQUETE")
                .build();
    }

    @Test
    void testObtenerTodos() {
        when(shipmentRepository.findAll()).thenReturn(List.of(shipment));
        List<Shipment> result = shipmentService.obtenerTodos();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerPorId() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        Optional<Shipment> result = shipmentService.obtenerPorId(1L);
        assertTrue(result.isPresent());
        assertEquals("PREPARANDO PAQUETE", result.get().getEstadoActual());
    }

    @Test
    void testCrearEnvio() {
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);

        Shipment resultado = shipmentService.crearEnvio(shipmentDTO);
        
        assertNotNull(resultado);
        assertEquals("PREPARANDO PAQUETE", resultado.getEstadoActual());
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void testAgregarHistorial() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        
        Shipment actualizado = new Shipment();
        actualizado.setEstadoActual("EN RUTA");
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(actualizado);

        TrackingDTO trackingDTO = new TrackingDTO();
        trackingDTO.setNuevaDescripcion("EN RUTA");

        Shipment resultado = shipmentService.agregarHistorial(1L, trackingDTO);
        assertNotNull(resultado);
        assertEquals("EN RUTA", resultado.getEstadoActual());
    }

    @Test
    void testEliminar() {
        when(shipmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(shipmentRepository).deleteById(1L);
        
        assertDoesNotThrow(() -> shipmentService.eliminar(1L));
        verify(shipmentRepository, times(1)).deleteById(1L);
    }
}
