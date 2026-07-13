package com.notificiones_service.notificiones_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.notificiones_service.notificiones_service.DTO.NotificacionDTO;
import com.notificiones_service.notificiones_service.model.Notificacion;
import com.notificiones_service.notificiones_service.repository.NotificacionRepository;
import com.notificiones_service.notificiones_service.exception.ResourceNotFoundException;
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
class NotificacionServiceTest {

    @InjectMocks
    private NotificacionService notificacionService;

    @Mock
    private NotificacionRepository notificacionRepository;

    private Notificacion notificacion;
    private NotificacionDTO notificacionDTO;

    @BeforeEach
    void setUp() {
        notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setMensaje("Tu pedido está en camino");
        notificacion.setDestinatario("juan@test.com");
        notificacion.setEstado("CREADA");
        notificacion.setFecha(LocalDateTime.now());

        notificacionDTO = NotificacionDTO.builder()
                .id(1L)
                .mensaje("Tu pedido está en camino")
                .destinatario("juan@test.com")
                .build();
    }

    @Test
    void testObtenerTodas() {
        when(notificacionRepository.findAll()).thenReturn(List.of(notificacion));
        List<Notificacion> resultado = notificacionService.obtenerTodas();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testObtenerPorId() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        Optional<Notificacion> resultado = notificacionService.obtenerPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("juan@test.com", resultado.get().getDestinatario());
    }

    @Test
    void testCrearNotificacion() {
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        Notificacion resultado = notificacionService.crearNotificacion(notificacionDTO);
        
        assertNotNull(resultado);
        assertEquals("CREADA", resultado.getEstado());
        verify(notificacionRepository).save(any(Notificacion.class));
    }

    @Test
    void testEliminarNotificacion() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(notificacionRepository).deleteById(1L);

        assertDoesNotThrow(() -> notificacionService.eliminarNotificacion(1L));
        verify(notificacionRepository, times(1)).deleteById(1L);
    }
}
