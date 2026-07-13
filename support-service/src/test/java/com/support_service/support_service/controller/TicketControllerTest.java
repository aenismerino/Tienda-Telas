package com.support_service.support_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.support_service.support_service.DTO.TicketDTO;
import com.support_service.support_service.model.Ticket;
import com.support_service.support_service.service.TicketService;
import com.support_service.support_service.assemblers.TicketModelAssembler;
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
public class TicketControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @Mock
    private TicketModelAssembler assembler;

    @InjectMocks
    private TicketControllerV2 controller;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Ticket ticket;
    private TicketDTO ticketDTO;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setAsunto("Ayuda");
        ticket.setDescripcion("No puedo comprar");
        ticket.setEstado("ABIERTO");
        ticket.setFecha(LocalDateTime.now());

        ticketDTO = TicketDTO.fromModel(ticket);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListarTicketsV2() throws Exception {
        when(ticketService.obtenerTodos()).thenReturn(List.of(ticket));
        
        EntityModel<TicketDTO> entityModel = EntityModel.of(ticketDTO,
                linkTo(methodOn(TicketControllerV2.class).obtenerTicketV2(1L)).withSelfRel());
                
        when(assembler.toModel(any(TicketDTO.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/support/v2/tickets"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCrearTicketV2() throws Exception {
        when(ticketService.crearTicket(any(TicketDTO.class))).thenReturn(ticket);
        
        EntityModel<TicketDTO> entityModel = EntityModel.of(ticketDTO,
                linkTo(methodOn(TicketControllerV2.class).obtenerTicketV2(1L)).withSelfRel());
                
        when(assembler.toModel(any(TicketDTO.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/support/v2/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testEliminarTicketV2() throws Exception {
        doNothing().when(ticketService).eliminarTicket(1L);

        mockMvc.perform(delete("/api/support/v2/tickets/1"))
                .andExpect(status().isNoContent());

        verify(ticketService, times(1)).eliminarTicket(1L);
    }
}
