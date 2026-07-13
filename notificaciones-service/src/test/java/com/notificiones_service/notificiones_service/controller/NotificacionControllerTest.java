package com.notificiones_service.notificiones_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificiones_service.notificiones_service.DTO.NotificacionDTO;
import com.notificiones_service.notificiones_service.model.Notificacion;
import com.notificiones_service.notificiones_service.service.NotificacionService;
import com.notificiones_service.notificiones_service.assemblers.NotificacionModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NotificacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private NotificacionModelAssembler assembler;

    @InjectMocks
    private NotificacionControllerV2 controller;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Notificacion notificacion;
    private NotificacionDTO notificacionDTO;

    @BeforeEach
    void setUp() {
        notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setMensaje("Hola");
        notificacion.setDestinatario("juan@test.com");
        notificacion.setEstado("CREADA");
        notificacion.setFecha(LocalDateTime.now());

        notificacionDTO = NotificacionDTO.fromModel(notificacion);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListarNotificacionesV2() throws Exception {
        when(notificacionService.obtenerTodas()).thenReturn(List.of(notificacion));
        
        EntityModel<NotificacionDTO> entityModel = EntityModel.of(notificacionDTO,
                linkTo(methodOn(NotificacionControllerV2.class).obtenerNotificacionV2(1L)).withSelfRel());
        
        when(assembler.toModel(any(NotificacionDTO.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/notificaciones/v2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCrearNotificacionV2() throws Exception {
        when(notificacionService.crearNotificacion(any(NotificacionDTO.class))).thenReturn(notificacion);
        
        EntityModel<NotificacionDTO> entityModel = EntityModel.of(notificacionDTO,
                linkTo(methodOn(NotificacionControllerV2.class).obtenerNotificacionV2(1L)).withSelfRel());
                
        when(assembler.toModel(any(NotificacionDTO.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/notificaciones/v2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacionDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testEliminarNotificacionV2() throws Exception {
        doNothing().when(notificacionService).eliminarNotificacion(1L);

        mockMvc.perform(delete("/api/notificaciones/v2/1"))
                .andExpect(status().isNoContent());

        verify(notificacionService, times(1)).eliminarNotificacion(1L);
    }
}
